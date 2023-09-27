package ru.practicum.events.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.events.model.Location;
import ru.practicum.util.Const;
import ru.practicum.util.EventStateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;

    @Size(max = 7000, min = 20)
    private String description;
    @Future
    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime eventDate;
    private Location location;

    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;

}
