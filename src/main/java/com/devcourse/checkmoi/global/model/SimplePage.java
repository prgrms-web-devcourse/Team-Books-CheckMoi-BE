package com.devcourse.checkmoi.global.model;


import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class SimplePage {

    private static final int LOWER_LIMIT_VALID_PAGE_SIZE = 1;

    private static final int LOWER_LIMIT_VALID_PAGE = 1;

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private Integer page;

    private Integer size;

    private Direction direction;

    @Builder
    public SimplePage(Integer page, Integer size, Direction direction) {
        this.page = isInvalidPage(page) ? DEFAULT_PAGE : page;
        this.size = isInvalidSize(size) ? DEFAULT_PAGE_SIZE : size;
        this.direction = isInvalidDirection(direction) ? Direction.ASC : direction;
    }

    public PageRequest pageRequest() {
        return PageRequest.of(page - 1, size, Direction.ASC, "createdAt");
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Direction getDirection() {
        return direction;
    }

    private boolean isInvalidPage(Integer page) {
        return page == null || page < LOWER_LIMIT_VALID_PAGE;
    }

    private boolean isInvalidSize(Integer size) {
        return size == null || size < LOWER_LIMIT_VALID_PAGE_SIZE;
    }

    public boolean isInvalidDirection(Direction direction) {
        return direction == null;
    }
}
