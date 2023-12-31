package ru.practicum.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataNotCorrectException(final DataNotCorrectException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("Request is not correctly")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(final DataNotFoundException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(Throwable e) {
        return Map.of(
                "status", "BAD_REQUEST",
                "message", e.getMessage()

        );
    }
}
