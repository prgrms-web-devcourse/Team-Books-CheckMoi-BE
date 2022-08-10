package com.devcourse.checkmoi.domain.post.model;

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

    public boolean isAllowedWriter(StudyMember member) {
        return allowedMembers.contains(member.getStatus());
    }
}
