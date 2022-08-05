package com.devcourse.checkmoi.domain.study.service.validator;


import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.DENIED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyServiceValidatorImplTest {

    StudyServiceValidator studyServiceValidator = new StudyServiceValidatorImpl();

    @Test
    @DisplayName("해당하는 스터디가 존재하는지 검사")
    void validateExistStudy() {
        studyServiceValidator.validateExistStudy(true);
        assertThrows(StudyNotFoundException.class,
            () -> studyServiceValidator.validateExistStudy(false));
    }

    @Test
    @DisplayName("해당하는 아이디는 스터디장의 아이디와 일치하는지 검사")
    void validateStudyOwner() {
        studyServiceValidator.validateStudyOwner(1L, 1L, "message");
        assertThrows(NotStudyOwnerException.class,
            () -> studyServiceValidator.validateStudyOwner(1L, 2L, "스터디장은 2L"));
    }

    @Test
    @DisplayName("스터디에 중복해서 가입신청하는지 검사")
    void validateDuplicateStudyMemberRequest() {
        User user = makeUser();
        studyServiceValidator.validateDuplicateStudyMemberRequest(
            makeStudyMember(makeStudy(makeBook(), RECRUITING), user, DENIED));

        assertThrows(DuplicateStudyJoinRequestException.class,
            () -> studyServiceValidator.validateDuplicateStudyMemberRequest(
                makeStudyMember(makeStudy(makeBook(), RECRUITING), user, PENDING)));
    }
}
