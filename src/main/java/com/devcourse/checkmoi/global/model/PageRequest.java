package com.devcourse.checkmoi.global.model;

import org.springframework.data.domain.Sort.Direction;

public class PageRequest {

    private int page = 1;

    private int size = 10;

    private Direction direction = Direction.DESC;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int defaultSize = 10;
        int maxSize = 50;
        this.size = size > maxSize ? defaultSize : size;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction,
            "create_date");
    }
}
