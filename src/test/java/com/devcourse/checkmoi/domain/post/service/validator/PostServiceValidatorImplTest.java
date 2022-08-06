package com.devcourse.checkmoi.domain.post.service.validator;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.devcourse.checkmoi.domain.post.exception.PostNoPermissionException;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServiceValidatorImplTest {

    PostServiceValidator postValidator = new PostServiceValidatorImpl();

    @Test
    @DisplayName("해당하는 스터디가 존재하는지 검사")
    void validatePostOwner() {
        User owner = makeUserWithId(1L);
        User illegalUser = makeUserWithId(2L);

        postValidator.validatePostOwner(owner.getId(), 1L);
        assertThrows(PostNoPermissionException.class,
            () -> postValidator.validatePostOwner(owner.getId(), illegalUser.getId()));
    }

}
