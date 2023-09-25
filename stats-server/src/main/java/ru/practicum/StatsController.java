package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.GetEndpointHitDto;
import ru.practicum.model.dto.RequestEndpointHitDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class StatsController {

    private static final String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestEndpointHitDto createHit(@RequestBody @Valid GetEndpointHitDto hit) {
        log.info("Create hit {}", hit);
        return statsService.create(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getUri(@RequestParam @DateTimeFormat(pattern = DATA_FORMAT_PATTERN) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = DATA_FORMAT_PATTERN) LocalDateTime end,
                                  @RequestParam(required = false) String[] uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get uri  from={}, to={}", start, end);
        return statsService.getHit(start, end, uris, unique);
    }
}
