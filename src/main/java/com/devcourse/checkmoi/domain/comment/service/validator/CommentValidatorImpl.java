package com.devcourse.checkmoi.domain.comment.service.validator;

import com.devcourse.checkmoi.domain.comment.exception.CommentNoPermissionException;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.stereotype.Component;

@Component
public class CommentValidatorImpl implements CommentValidator {

    @Override
    public void commentPermission(Long userId, Long... compareId) {
        boolean permissionUser = false;
        for (Long id : compareId) {
            if (userId.equals(id)) {
                permissionUser = true;
                break ;
            }
        }
        if (!permissionUser) {
            throw new CommentNoPermissionException();
        }
    }
}
