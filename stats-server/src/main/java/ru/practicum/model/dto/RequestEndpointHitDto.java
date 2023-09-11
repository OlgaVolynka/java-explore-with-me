package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class RequestEndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

}
