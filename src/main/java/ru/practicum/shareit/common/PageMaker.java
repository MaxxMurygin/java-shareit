package ru.practicum.shareit.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageMaker {
    private static final Integer DEFAULT_START = 0;
    private static final Integer DEFAULT_SIZE = 100;

    public static Pageable make(Integer from, Integer size) {
        if (from == null) {
            from = DEFAULT_START;
        }
        if (size == null) {
            size = DEFAULT_SIZE;
        }
        if (from < 0) {
            throw new IllegalArgumentException("From parameter should be zero or positive");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size parameter should be positive");
        }
        return PageRequest.of(from, size);
    }

    public static Pageable make(Integer from, Integer size, Sort sort) {
        if (from == null) {
            from = DEFAULT_START;
        }
        if (size == null) {
            size = DEFAULT_SIZE;
        }
        if (from < 0) {
            throw new IllegalArgumentException("From parameter should be zero or positive");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size parameter should be positive");
        }
        return PageRequest.of(from, size, sort);
    }
}
