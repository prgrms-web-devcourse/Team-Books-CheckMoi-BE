package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.converter.CommentConverter;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import com.devcourse.checkmoi.domain.comment.service.validator.CommentServiceValidator;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;

    private final StudyRepository studyRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentServiceValidator commentServiceValidator;

    private final StudyMemberRepository studyMemberRepository;

    private final CommentConverter commentConverter;

    @Override
    public void deleteById(Long userId, Long commentId) {
        // TODO: validation
        commentRepository.deleteById(commentId);
    }

    @Override
    public Long createComment(Long studyId, Long postId, Long userId, Create request) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        commentServiceValidator.finishedStudy(study.getStatus());
        commentServiceValidator.existPost(postRepository.existsById(postId));
        commentServiceValidator.existUser(userRepository.existsById(userId));
        commentServiceValidator.joinStudyUser(studyMemberRepository.findByUserId(userId).isPresent());
        return commentRepository.save(
            commentConverter.createToComment(request, postId, userId))
            .getId();
    }

}
