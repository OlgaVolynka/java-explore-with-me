package ru.practicum.events.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.events.dto.location.LocationDto;
import ru.practicum.util.Const;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @Future
    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid = false;

    @PositiveOrZero
    private Long participantLimit = 0L;

    @NotNull
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

}
