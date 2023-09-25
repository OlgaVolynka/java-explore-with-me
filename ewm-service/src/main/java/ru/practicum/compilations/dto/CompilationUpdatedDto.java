package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdatedDto {

    @Size(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    private Set<Long> events;

}
