package com.devcourse.checkmoi.domain.post.service.validator;

import com.devcourse.checkmoi.domain.post.exception.NotAllowedWriterException;
import com.devcourse.checkmoi.domain.post.exception.PostNoPermissionException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.study.exception.ClosedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import org.springframework.stereotype.Component;

@Component
public class PostServiceValidatorImpl implements PostServiceValidator {

    @Override
    public void checkPostOwner(Long userId, Long writerId) {
        if (!userId.equals(writerId)) {
            throw new PostNoPermissionException();
        }
    }

    @Override
    public void checkPermissionToDelete(StudyMember member, Post post) {
        if (!member.isOwner()) {
            Long postWriterId = post.getWriter().getId();
            Long memberId = member.getUser().getId();

            if (!postWriterId.equals(memberId)) {
                throw new PostNoPermissionException();
            }
        }
    }

    @Override
    public void checkJoinedMember(StudyMember studyMember, Long studyId) {
        if (!studyMember.isJoined()) {
            throw new NotJoinedMemberException(
                "스터디멤버 id" + studyMember.getId() + " 는 해당 스터디 멤버가 아닙니다 - studyId : " + studyId);
        }
    }

    @Override
    public void checkAllowedWriter(Post post, StudyMember member) {
        if (!post.isAllowedMember(member)) {
            throw new NotAllowedWriterException(
                "게시글 작성권한이 없습니다 - 요청한 게시글 카테고리 {}" + this + "요청한 멤버 : " + member.getId());
        }
    }

    @Override
    public void checkWritingAllowedPost(Post post, Long studyId) {
        if (!post.isAllowedToBeWritten()) {
            throw new ClosedStudyException("진행 완료된 스터디입니다. studyId :" + studyId);
        }
    }
}
