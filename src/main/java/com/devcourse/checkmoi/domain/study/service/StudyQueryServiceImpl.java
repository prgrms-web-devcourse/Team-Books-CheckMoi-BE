package com.devcourse.checkmoi.domain.study.service;

import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyQueryServiceImpl implements StudyQueryService {

    private final StudyConverter studyConverter;

    private final StudyRepository studyRepository;

    private final UserRepository userRepository;

    private final StudyServiceValidator studyValidator;

    @Override
    public Studies getStudies(Long bookId, Pageable pageable) {
        return new Studies(
            studyRepository.findRecruitingStudyByBookId(bookId, pageable)
                .stream()
                .map(studyConverter::studyToStudyInfo)
                .toList()
        );
    }

    @Override
    public StudyDetailWithMembers getStudyInfoWithMembers(Long studyId) {
        return studyRepository.getStudyInfoWithMembers(studyId);
    }

    @Override
    public StudyAppliers getStudyAppliers(Long userId, Long studyId) {
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);

        studyValidator.validateStudyOwner(userId, studyOwnerId,
            "스터디 신청 목록 조회 권한이 없습니다. 스터디 Id : " + studyId + "유저 Id : " + userId + " 스터디 장 Id : "
                + studyOwnerId
        );

        return studyRepository.getStudyAppliers(studyId);
    }

    @Override
    public List<List<MyStudyInfo>> getMyStudies(Long userId) {
        List<MyStudyInfo> studies = studyRepository.getMyStudies(userId);
        List<MyStudyInfo> progress = new ArrayList<>();
        List<MyStudyInfo> finished = new ArrayList<>();
        List<MyStudyInfo> owned = new ArrayList<>();

        for (MyStudyInfo study : studies) {
            if (IN_PROGRESS.name().equals(study.category())) {
                progress.add(study);
            } else if (FINISHED.name().equals(study.category())) {
                finished.add(study);
            }
            if (study.isOwner()) {
                owned.add(study);
            }
        }
        return List.of(
            progress,
            finished,
            owned
        );
    }

}
