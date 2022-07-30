package com.devcourse.checkmoi.domain.study.repository.study;

import static com.devcourse.checkmoi.domain.user.model.UserRole.GUEST;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StudyRepositoryTest extends RepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("스터디 수정 #30")
    class findStudyOwnerTest {

        @Test
        @DisplayName("해당 스터디의 스터디장의 ID를 찾는다")
        void test() {
            Long want = 1L;
            String name = "name";
            User user = userRepository.saveAndFlush(
                User.builder().oauthId(name).provider("kakao").name(name)
                    .email(new Email(name + "@gmail.com")).userRole(GUEST).profileImgUrl("url")
                    .build()
            );
            Study study = studyRepository.saveAndFlush(
                Study.builder()
                    .id(1L)
                    .build()
            );
            studyMemberRepository.saveAndFlush(
                StudyMember.builder()
                    .id(1L)
                    .status(StudyMemberStatus.OWNED)
                    .study(study)
                    .user(
                        user
                    )
                    .build()
            );
            Long got = studyRepository.findStudyOwner(1L);

            assertThat(got).isEqualTo(want);
        }
    }
}