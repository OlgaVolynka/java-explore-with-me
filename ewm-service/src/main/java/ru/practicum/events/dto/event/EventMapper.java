package ru.practicum.events.dto.event;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.dto.location.LocationMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

@UtilityClass
public class EventMapper {

    public static EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .location(LocationMapper.locationToDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .state(event.getState())
                .description(event.getDescription())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .requestModeration(event.getRequestModeration())
                .publishedOn(event.getPublishedOn())
                .build();
    }

    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event newEventDtoToEvent(NewEventDto eventDto, Location location, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLocation(location);
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setInitiator(user);
        return event;
    }

}
