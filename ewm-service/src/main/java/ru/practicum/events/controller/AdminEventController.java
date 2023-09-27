package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.event.EventFullDto;

import ru.practicum.events.dto.event.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.util.Const;
import ru.practicum.util.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<EventState> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = Const.DATA_FORMAT_PATTERN) LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = Const.DATA_FORMAT_PATTERN) LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "0")
                                              @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10")
                                              @Positive Integer size) {
        log.info("Get events of users {}", users);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable(value = "eventId") Long eventId,
                                         @Valid @RequestBody UpdateEventAdminRequest eventAdminRequest) {
        log.info("Update event {} with id= {}", eventAdminRequest, eventId);
        return eventService.updateEventAdmin(eventId, eventAdminRequest);
    }

}
