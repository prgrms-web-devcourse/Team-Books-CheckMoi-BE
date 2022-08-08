package com.devcourse.checkmoi.domain.post.model;

import com.devcourse.checkmoi.domain.post.exception.NotAllowedWriterException;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import java.util.Set;

public enum PostCategory {
    NOTICE(Set.of(StudyMemberStatus.OWNED)),
    GENERAL(Set.of(StudyMemberStatus.OWNED, StudyMemberStatus.ACCEPTED)),
    BOOK_REVIEW(Set.of(StudyMemberStatus.OWNED, StudyMemberStatus.ACCEPTED));

    private final Set<StudyMemberStatus> allowedMembers;

    PostCategory(
        Set<StudyMemberStatus> allowedMembers) {
        this.allowedMembers = allowedMembers;
    }

    public void checkAllowedWriter(StudyMember member) {
        if (!allowedMembers.contains(member.getStatus())) {
            throw new NotAllowedWriterException(
                "게시글 작성권한이 없습니다 - 요청한 게시글 카테고리 {}" + this + "요청한 멤버 : " + member.getId());
        }
    }
}
