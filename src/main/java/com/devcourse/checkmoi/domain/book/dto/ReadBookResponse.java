package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.LatestAll;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.Simple;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.Specification;
import java.util.List;

public sealed interface ReadBookResponse permits Simple, Specification, LatestAll {

    record Simple(
        Long id,
        String title,
        String image,
        String author,
        String publisher,
        String pubDate,
        String isbn,
        String description,
        String createAt
    ) implements ReadBookResponse {

    }

    record Specification(
        Long id,
        String title,
        String image,
        String author,
        String publisher,
        String pubDate,
        String isbn,
        String description,
        String createAt
    ) implements ReadBookResponse {

    }

    record LatestAll(
        List<Simple> books
    ) implements ReadBookResponse {

    }
}
