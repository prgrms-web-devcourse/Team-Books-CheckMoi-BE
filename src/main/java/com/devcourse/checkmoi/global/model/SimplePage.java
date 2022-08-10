package com.devcourse.checkmoi.global.model;


import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class SimplePage {

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int page = DEFAULT_PAGE;

    private int size = DEFAULT_PAGE_SIZE;

    private Direction direction = Direction.DESC;

    @Builder
    public SimplePage() {

    }

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

    public PageRequest of() {
        return PageRequest.of(page - 1, size, direction, "createdAt");
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

}
