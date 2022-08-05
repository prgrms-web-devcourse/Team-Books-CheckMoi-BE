package com.devcourse.checkmoi.domain.post.repository;

import com.devcourse.checkmoi.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
