package ru.practicum.events.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.events.dto.location.LocationDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.util.Const;
import ru.practicum.util.EventState;

import java.time.LocalDateTime;

import static ru.practicum.util.EventState.PENDING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

    private boolean paid;

    private Long participantLimit;

    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private EventState state = PENDING;

    private String title;

    private long views;

}
