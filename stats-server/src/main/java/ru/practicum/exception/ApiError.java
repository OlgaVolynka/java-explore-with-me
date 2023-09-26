package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiError {

    private static final String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    private final String message;
    private final String reason;
    private final HttpStatus status;
    @JsonFormat(pattern = DATA_FORMAT_PATTERN)
    private final LocalDateTime timestamp;
}
