package ru.practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetEndpointHitDto {

    private static final String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    @JsonFormat(pattern = DATA_FORMAT_PATTERN)
    private LocalDateTime timestamp;

}
