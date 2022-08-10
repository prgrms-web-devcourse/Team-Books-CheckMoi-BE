package com.devcourse.checkmoi.domain.post.service.validator;

import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.study.model.StudyMember;

public interface PostServiceValidator {

    void checkPostOwner(Long userId, Long postId);

    void checkPermissionToDelete(StudyMember member, Post post);

    void checkJoinedMember(StudyMember studyMember, Long studyId);

    void checkAllowedWriter(Post post, StudyMember member);

    void checkWritingAllowedPost(Post post, Long studyId);
}
