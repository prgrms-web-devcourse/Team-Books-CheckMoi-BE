package com.devcourse.checkmoi.domain.post.model;

import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.global.annotation.CodeMappable;
import java.util.Set;

public enum PostCategory implements CodeMappable {
    NOTICE("NOTICE", Set.of(StudyMemberStatus.OWNED)),
    GENERAL("GENERAL", Set.of(StudyMemberStatus.OWNED, StudyMemberStatus.ACCEPTED)),
    BOOK_REVIEW("BOOK_REVIEW", Set.of(StudyMemberStatus.OWNED, StudyMemberStatus.ACCEPTED));

    private final Set<StudyMemberStatus> allowedMembers;

    private final String code;

    PostCategory(
        String code,
        Set<StudyMemberStatus> allowedMembers) {

        this.code = code;
        this.allowedMembers = allowedMembers;
    }

    @Override
    public String getMappingCode() {
        return this.code;
    }

    public boolean isAllowedWriter(StudyMember member) {
        return allowedMembers.contains(member.getStatus());
    }
}
