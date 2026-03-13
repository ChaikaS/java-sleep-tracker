package ru.yandex.practicum.sleeptracker.exception;

public class EmptySessionsException extends RuntimeException {

    public EmptySessionsException(String message) {
        super(message);
    }
}
