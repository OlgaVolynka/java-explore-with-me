package ru.practicum.requests;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateRequestController {

    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable(value = "userId") Long userId,
                                          @RequestParam(value = "eventId") Long eventId) {
        log.info("Create participation request by event id= {} for user id= {} ", eventId, userId);
        return requestService.createParticipationRequest(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getParticipationRequest(@PathVariable(value = "userId") Long userId,
                                                                       @PathVariable(value = "eventId") Long eventId) {
        log.info("Get participation request for event by id= {} and user by id{}", eventId, userId);
        return requestService.getParticipationRequestPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatus(@PathVariable(value = "userId") Long userId,
                                                                   @PathVariable(value = "eventId") Long eventId,
                                                                   @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.info("Update participation request for event by id= {} and user by id{}", eventId, userId);
        return requestService.updateEventRequestStatusPrivate(userId, eventId, updateRequest);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable(value = "userId") Long userId) {
        log.info("Get participation requests by id= {} ", userId);
        return requestService.getParticipationRequestByUserId(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto updateParticipationRequestStatusToCancel(@PathVariable(value = "userId") Long userId,
                                                                            @PathVariable(value = "requestId") Long requestId) {
        log.info("Update participation request by event id= {} and user by id= {} ", requestId, userId);
        return requestService.updateStatusParticipationRequest(userId, requestId);
    }

}
