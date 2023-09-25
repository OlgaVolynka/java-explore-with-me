package ru.practicum.exception;

public class UnavalibleException extends RuntimeException {
    public UnavalibleException(String message) {
        super(message);
    }
}