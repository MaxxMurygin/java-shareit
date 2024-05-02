package ru.practicum.shareit.common;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(Class<?> entityClass, String message) {
        super("Entity " + entityClass.getSimpleName() + " already exist. " + message);
    }
}
