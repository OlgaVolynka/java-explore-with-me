package ru.practicum.compilations.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.events.dto.event.EventShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {

    private long id;
    private String title;
    private boolean pinned;
    private Set<EventShortDto> events;

}
