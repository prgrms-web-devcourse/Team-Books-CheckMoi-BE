package com.devcourse.checkmoi.domain.comment.repository;

import com.devcourse.checkmoi.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {


}
