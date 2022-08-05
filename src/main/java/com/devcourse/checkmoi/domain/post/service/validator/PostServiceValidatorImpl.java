package com.devcourse.checkmoi.domain.post.service.validator;

import com.devcourse.checkmoi.domain.post.exception.PostNoPermissionException;
import org.springframework.stereotype.Component;

@Component
public class PostServiceValidatorImpl implements PostServiceValidator {

    @Override
    public void validatePostOwner(Long userId, Long postedUserId) {
        if (!userId.equals(postedUserId)) {
            throw new PostNoPermissionException();
        }
    }
}
