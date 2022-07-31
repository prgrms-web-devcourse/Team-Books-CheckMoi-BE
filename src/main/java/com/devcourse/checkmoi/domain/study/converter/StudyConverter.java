package com.devcourse.checkmoi.domain.study.converter;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.stereotype.Component;

@Component
public class StudyConverter {

    public Study createToEntity(Create request) {
        return Study.builder()
            .book(
                Book.builder()
                    .id(request.bookId())
                    .build()
            )
            .name(request.name())
            .thumbnailUrl(request.thumbnail())
            .description(request.description())
            .maxParticipant(request.maxParticipant())
            .gatherStartDate(request.gatherStartDate())
            .gatherEndDate(request.gatherEndDate())
            .build();
    }

    public StudyInfo studyToStudyInfo(Study study) {
        return StudyInfo.builder()
            .id(study.getId())
            .name(study.getName())
            .thumbnailUrl(study.getThumbnailUrl())
            .description(study.getDescription())
            .currentParticipant(study.getCurrentParticipant())
            .maxParticipant(study.getMaxParticipant())
            .gatherStartDate(study.getGatherStartDate())
            .gatherEndDate(study.getGatherEndDate())
            .studyStartDate(study.getStudyStartDate())
            .studyEndDate(study.getStudyEndDate())
            .build();
    }
}
