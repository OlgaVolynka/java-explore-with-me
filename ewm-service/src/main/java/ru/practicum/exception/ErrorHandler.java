package ru.practicum.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e, WebRequest request) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getLocalizedMessage())
                .reason("Object not found " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataNotFoundException(final DataNotFoundException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("Request is not correctly")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()).build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataNotCorrectException(final DataNotCorrectException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("Request is not correctly")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidException(final RuntimeException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("Request is not correctly")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now()).build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final UnavalibleException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("The required object was found.")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now()).build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(final HttpServerErrorException.InternalServerError e, WebRequest request) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason(request.getDescription(false))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(RuntimeException e) {

        return new ApiError.ApiErrorBuilder()
                .message(e.getMessage())
                .reason("Constraint Violation Exception")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now()).build();

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Throwable e) {
        return Map.of(
                "status", "INTERNAL SERVER ERROR",
                "message", e.getMessage()

        );
    }
}
