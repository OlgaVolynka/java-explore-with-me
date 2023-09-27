package ru.practicum.events.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.util.Const;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {

    private String title;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private Long views;

}
