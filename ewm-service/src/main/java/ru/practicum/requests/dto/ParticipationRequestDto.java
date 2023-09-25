package ru.practicum.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.util.Const;
import ru.practicum.util.EventRequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ParticipationRequestDto {

   @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private EventRequestStatus status;

}
