package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.CompilationService;
import ru.practicum.compilations.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCompilationController {

    private final CompilationService serviceCompilation;

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        log.info("Get compilation by id= {}", compId);
        return serviceCompilation.getCompilationById(compId);
    }

    @GetMapping
    public Collection<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
                                          @RequestParam(defaultValue = "0")
                                          @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10")
                                          @Positive Integer size) {
        log.info("Get compilations pinned {}", pinned);
        return serviceCompilation.getAllCompilations(pinned, from, size);
    }
}
