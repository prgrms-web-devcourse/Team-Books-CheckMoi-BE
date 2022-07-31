package com.devcourse.checkmoi.domain.book.dto;


public class SimplePage {

    private static final int LOWER_LIMIT_VALID_PAGE_SIZE = 1;

    private static final int LOWER_LIMIT_VALID_PAGE = 0;

    private static final int DEFAULT_PAGE = 0;

    private static final int DEFAULT_PAGE_SIZE = 8;

    private final Integer page;

    private final Integer size;

    public SimplePage(Integer page, Integer size) {
        this.page = isInvalidPage(page) ? DEFAULT_PAGE : page;
        this.size = isInvalidSize(size) ? DEFAULT_PAGE_SIZE : size;
    }

    public static SimplePage defaultPage() {
        return new SimplePage(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    private boolean isInvalidPage(Integer page) {
        return page == null || page < LOWER_LIMIT_VALID_PAGE;
    }

    private boolean isInvalidSize(Integer size) {
        return size == null || size < LOWER_LIMIT_VALID_PAGE_SIZE;
    }
}
