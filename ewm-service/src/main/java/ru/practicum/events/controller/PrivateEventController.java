package ru.practicum.events.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.event.EventFullDto;
import ru.practicum.events.dto.event.EventShortDto;
import ru.practicum.events.dto.event.NewEventDto;
import ru.practicum.events.dto.event.UpdateEventUserRequest;
import ru.practicum.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable(value = "userId") Long userId,
                                    @Valid @RequestBody NewEventDto eventDto) {
        log.info("Create event {}", eventDto);
        return eventService.createEvent(userId, eventDto);
    }


    @GetMapping
    public Collection<EventShortDto> getEventsByUserId(@PathVariable(value = "userId") Long userId,
                                                       @RequestParam(value = "from", defaultValue = "0")
                                                       @PositiveOrZero Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                       @Positive Integer size) {
        log.info("Get events by user id= {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "userId") Long userId,
                                     @PathVariable(value = "eventId") Long eventId) {
        log.info("Get event by id= {} and user id= {}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "eventId") Long eventId,
                                        @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Updating event {} by id= {} and user by id= {}", eventDto, eventId, userId);
        return eventService.updateEventById(userId, eventId, eventDto);
    }
}
