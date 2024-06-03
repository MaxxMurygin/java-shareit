package ru.practicum.shareit.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageMaker {

    public static Pageable make(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    public static Pageable make(Integer from, Integer size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }
}
