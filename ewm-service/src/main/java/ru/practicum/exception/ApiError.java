package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import ru.practicum.util.Const;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiError {
    private final String message;
    private final String reason;
    private final HttpStatus status;
    @JsonFormat(pattern = Const.DATA_FORMAT_PATTERN)
    private final LocalDateTime timestamp;
}
