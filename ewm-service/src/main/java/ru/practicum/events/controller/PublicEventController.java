package ru.practicum.events.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.event.EventFullDto;
import ru.practicum.events.dto.event.EventShortDto;
import ru.practicum.events.service.EventService;
import ru.practicum.util.Const;
import ru.practicum.util.EventsSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicEventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(value = "id") Long id,
                                     HttpServletRequest request) {
        log.info("Get event by id= {}", id);
        return eventService.getEventByIdPublic(id, request.getRemoteAddr());
    }

    @GetMapping
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = Const.DATA_FORMAT_PATTERN) LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = Const.DATA_FORMAT_PATTERN) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                               @RequestParam(defaultValue = "0")
                                               @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10")
                                               @Positive Integer size,
                                               HttpServletRequest request
    ) {
        EventsSort sortParam = EventsSort.from(sort).orElseThrow(() -> new ValidationException("Sort isn't valid: "
                + sort));

        log.info("Get public events by text {}, categories {} onlyAvailable {}, sort {}", text, categories,
                onlyAvailable, sort);
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sortParam, from, size, request);
    }
}

