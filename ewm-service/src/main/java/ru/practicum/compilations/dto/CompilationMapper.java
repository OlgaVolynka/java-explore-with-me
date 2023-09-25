package ru.practicum.compilations.dto;


import lombok.experimental.UtilityClass;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.event.EventMapper;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto compilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents() != null
                        ? compilation.getEvents()
                        .stream()
                        .map(EventMapper::eventToShortDto)
                        .collect(Collectors.toSet())
                        : null)
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation compilationDtoToNewCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());

        return compilation;
    }
}
