package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.ParticipationRequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.requests.dto.ParticipationRequestMapper.createNewParticipationRequest;
import static ru.practicum.requests.dto.ParticipationRequestMapper.requestToParticipationRequestDto;
import static ru.practicum.util.EventRequestStatus.*;
import static ru.practicum.util.EventState.PUBLISHED;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {

        User user = shackIdUser(userId);

        Event event = shackIdEvent(eventId);

        User owner = event.getInitiator();

        if (userId.equals(owner.getId())) {
            throw new ValidateException("Owner cannot send request");
        }
        event.setConfirmedRequests(requestRepository
                .countRequestByEventIdAndStatus(event.getId(), CONFIRMED));
        shackParticipantLimit(event);

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ValidateException("You can't create same request twice");
        }

        if (!event.getState().equals(PUBLISHED)) {
            throw new ValidateException("Event isn't published ");
        }

        ParticipationRequest participationRequest = requestRepository.save(createNewParticipationRequest(event, user));

        return requestToParticipationRequestDto(participationRequest);
    }


    public List<ParticipationRequestDto> getParticipationRequestPrivate(Long userId, Long eventId) {

        shackIdEvent(eventId);
        shackIdUser(userId);
        if (eventRepository.findByIdAndInitiatorId(eventId, userId).isPresent()) {
            return requestRepository.findAllByEventId(eventId).stream()
                    .map(ParticipationRequestMapper::requestToParticipationRequestDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestStatusPrivate(Long userId, Long eventId,
                                                                          EventRequestStatusUpdateRequest statusUpdateRequest) {

        shackIdUser(userId);
        Event event = shackIdEvent(eventId);

        event.setConfirmedRequests(requestRepository
                .countRequestByEventIdAndStatus(event.getId(), CONFIRMED));

        shackParticipantLimit(event);

        if (!event.getRequestModeration()) {
            throw new ValidateException("It isn't possible to update status");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndIdIn(eventId,
                statusUpdateRequest.getRequestIds());


        switch (statusUpdateRequest.getStatus()) {
            case CONFIRMED:
                return createConfirmedStatus(requests, event);
            case REJECTED:
                return createRejectedStatus(requests);
            default:
                throw new ValidateException("Not correct State: " + statusUpdateRequest.getStatus());
        }
    }

    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId) {
        shackIdUser(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto updateStatusParticipationRequest(Long userId, Long requestId) {
        shackIdUser(userId);
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Not found Request by id=" + requestId));
        request.setStatus(CANCELED);
        return requestToParticipationRequestDto(requestRepository.save(request));
    }


    //PrivateMethods:
    private EventRequestStatusUpdateResult createConfirmedStatus(List<ParticipationRequest> requests, Event event) {
        shackParticipantLimit(event);
        long limitRequest = event.getParticipantLimit() - event.getConfirmedRequests();

        List<ParticipationRequest> confirmedRequests;
        List<ParticipationRequest> rejectedRequests;

        if (requests.size() <= limitRequest) {
            confirmedRequests = requests.stream()
                    .peek(request -> request.setStatus(CONFIRMED))
                    .collect(Collectors.toList());
            rejectedRequests = List.of();
        } else {
            confirmedRequests = requests.stream()
                    .limit(limitRequest)
                    .peek(request -> request.setStatus(CONFIRMED))
                    .collect(Collectors.toList());
            rejectedRequests = requests.stream()
                    .skip(limitRequest)
                    .peek(request -> request.setStatus(REJECTED))
                    .collect(Collectors.toList());
        }

        event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());

        List<ParticipationRequest> updatedRequests = Stream.concat(confirmedRequests.stream(), rejectedRequests.stream())
                .collect(Collectors.toList());
        requestRepository.saveAll(updatedRequests);

        List<ParticipationRequestDto> confirmedRequestsDto = confirmedRequests.stream()
                .map(ParticipationRequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> rejectedRequestsDto = rejectedRequests.stream()
                .map(ParticipationRequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());

        return new EventRequestStatusUpdateResult(confirmedRequestsDto, rejectedRequestsDto);
    }

    private EventRequestStatusUpdateResult createRejectedStatus(List<ParticipationRequest> requests) {

        boolean isStatusPending = requests.stream()
                .anyMatch(request -> !request.getStatus().equals(PENDING));
        if (isStatusPending) {
            throw new ValidateException("Request status can't be change'");
        }

        requests.forEach(request -> request.setStatus(REJECTED));
        requestRepository.saveAll(requests);
        List<ParticipationRequestDto> rejectedRequests = requests
                .stream()
                .map(ParticipationRequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(List.of(), rejectedRequests);
    }

    private void shackParticipantLimit(Event event) {
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ValidateException("All event participants have been recruited");
        }
    }

    private User shackIdUser(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Not found User by id=" + id));
    }

    private Event shackIdEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Not found Event by id=" + id));
    }
}