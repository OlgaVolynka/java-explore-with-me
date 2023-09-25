package ru.practicum.events.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.event.*;
import ru.practicum.events.dto.location.LocationMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.repository.LocationRepository;
import ru.practicum.exception.*;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.GetEndpointHitDto;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.EventState;
import ru.practicum.util.EventStateAction;
import ru.practicum.util.EventsSort;
import ru.practicum.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.util.EventState.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@PropertySource(value = {"classpath:application.properties"})
public class EventService {

    @Value("${app}")
    String app;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final LocationRepository locationRepository;

    //AdminEventController methods:
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest eventDto) {

        Event event = shackAndGetEventById(eventId);

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(shackAndGetCategory(eventDto.getCategory()));
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }

        if (eventDto.getStateAction() != null) {
            if (event.getState().equals(PENDING)) {
                if (eventDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                    event.setState(CANCELED);
                }
                if (eventDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            } else {
                throw new ValidateException("Not the right state: " + event.getState());
            }
        }

        if (eventDto.getEventDate() != null && event.getState().equals(PUBLISHED)) {
            validateEventDate(eventDto.getEventDate());
            event.setEventDate(eventDto.getEventDate());
        }

        event = eventRepository.save(event);
        locationRepository.save(event.getLocation());
        return EventMapper.eventToEventFullDto(event);
    }


    public List<EventFullDto> getEvents(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size) {

        shackPeriodDate(rangeStart, rangeEnd);
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        PageRequest pageable = new Pagination(from, size, Sort.unsorted());
        List<Event> events = eventRepository.findAllForAdmin(users, states, categories, rangeStart, pageable);

        return events.stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
    }

    //PrivateEventController methods:

    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        validateEventDate(newEventDto.getEventDate());

        User user = shackIdUser(userId);
        Category category = shackAndGetCategory(newEventDto.getCategory());

        Location savedLocation = locationRepository
                .save(LocationMapper.dtoToLocation(newEventDto.getLocation()));

        Event event = eventRepository.save(EventMapper.newEventDtoToEvent(newEventDto, savedLocation, user, category));
        return EventMapper.eventToEventFullDto(event);
    }


    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {

        shackIdUser(userId);
        return eventRepository.findAllByInitiator_Id(userId, new Pagination(from, size,
                        Sort.unsorted())).stream()
                .map(EventMapper::eventToShortDto)
                .collect(Collectors.toList());
    }


    public EventFullDto getEventById(Long userId, Long eventId) {

        shackIdUser(userId);
        Event newEvent = shackAndGetEventById(eventId);
        return EventMapper.eventToEventFullDto(newEvent);
    }

    @Transactional
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequest eventUpdatedDto) {

        shackIdUser(userId);
        Event event = shackAndGetEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new UnavalibleException("only initiator can update event");
        }

        validateEventDate(event.getEventDate());
        if (event.getState() == PUBLISHED) {
            throw new ValidateException("Events can be updated, not correct status=" + event.getState());
        }

        if (eventUpdatedDto.getStateAction() != null) {
            if (eventUpdatedDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                event.setState(CANCELED);
            }
            if (eventUpdatedDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                event.setState(PENDING);
            }
        }
        if (eventUpdatedDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdatedDto.getAnnotation());
        }
        if (eventUpdatedDto.getDescription() != null) {
            event.setDescription(eventUpdatedDto.getDescription());
        }
        if (eventUpdatedDto.getCategory() != null) {
            event.setCategory(shackAndGetCategory(eventUpdatedDto.getCategory()));
        }
        if (eventUpdatedDto.getPaid() != null) {
            event.setPaid(eventUpdatedDto.getPaid());
        }
        if (eventUpdatedDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdatedDto.getParticipantLimit());
        }
        if (eventUpdatedDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdatedDto.getRequestModeration());
        }
        if (eventUpdatedDto.getTitle() != null) {
            event.setTitle(eventUpdatedDto.getTitle());
        }
        if (eventUpdatedDto.getLocation() != null) {
            event.setLocation(LocationMapper.dtoToLocation(eventUpdatedDto.getLocation()));
        }
        if (eventUpdatedDto.getEventDate() != null) {
            validateEventDate(eventUpdatedDto.getEventDate());
            event.setEventDate(eventUpdatedDto.getEventDate());
        }

        Event eventSaved = eventRepository.save(event);
        locationRepository.save(eventSaved.getLocation());

        return EventMapper.eventToEventFullDto(eventSaved);
    }

    //PublicEventController methods:


    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               EventsSort sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(100);
        if (rangeStart != null) {
            start = rangeStart;
        }
        if (rangeEnd != null) {
            end = rangeEnd;
        }

        shackPeriodDate(rangeStart, rangeEnd);
        List<EventShortDto> foundEvents = eventRepository
                .findEvents(EventState.PUBLISHED, text, categories, paid, start, end, onlyAvailable, PageRequest.of(from, size, Sort.by("id")))
                .stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
        GetEndpointHitDto hit = new GetEndpointHitDto(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());


        saveViewInEvent(foundEvents);
        statsClient.saveHit(hit);


        if (sort != null) {
            switch (sort.toString()) {
                case "EVENT_DATE":
                    foundEvents = foundEvents
                            .stream()
                            .sorted(Comparator.comparing(EventShortDto::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
                    foundEvents = foundEvents
                            .stream()
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new DataNotFoundException("cannot be sorted by %s" + sort);
            }
        }

        return foundEvents;

    }


    public EventFullDto getEventByIdPublic(Long eventId, String ip) {
        Event event = shackAndGetEventById(eventId);
        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Event with id=" + eventId + " hasn't not published");
        }
        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event);
        String uri;
        if (!eventId.equals(0L)) {
            uri = "/events/" + eventId;
        } else {
            uri = "/events";
        }
        GetEndpointHitDto endpointHit = new GetEndpointHitDto(app, uri, ip, LocalDateTime.now());
        statsClient.saveHit(endpointHit);

        String[] uris = new String[1];

        uris[0] = uri;
        List<ViewStats> views = statsClient.getStats(LocalDateTime.now().minusYears(200), LocalDateTime.now().plusYears(200), uris, false);

        eventFullDto.setViews(views.size());

        return eventFullDto;
    }

    //Private methods:

    private Category shackAndGetCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Category by id=" + id));
    }

    private User shackIdUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found User by id=" + id));
    }


    private Event shackAndGetEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Event by id=" + id));
    }

    private void shackPeriodDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new DataNotCorrectException("The range start date cannot be is after range end date");
            }
        }
    }

    private Long getViewId(String url) {
        String[] uri = url.split("/");
        return Long.valueOf(uri[uri.length - 1]);
    }

    private void saveViewInEvent(List<EventShortDto> result) {

        String[] uris = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            uris[i] = "/events/" + result.get(i).getId().toString();
        }

        List<Long> eventIds = result.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> start = eventRepository.getStart(eventIds);

        if (start.isPresent()) {
            List<ViewStats> views = statsClient.getStats(
                    start.get(),
                    LocalDateTime.now(),
                    uris,
                    true);

            if (views != null) {
                Map<Long, Long> mapIdHits = views.stream()
                        .collect(Collectors.toMap(viewStats -> getViewId(viewStats.getUri()), ViewStats::getHits));

                result.forEach(eventShortDto -> {
                    Long eventId = eventShortDto.getId();
                    Long viewsCount = mapIdHits.getOrDefault(eventId, 0L);
                    eventShortDto.setViews(viewsCount);
                });
            }
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {

        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataNotFoundException("Event date should be after now +2 hours");
        }
    }
}
