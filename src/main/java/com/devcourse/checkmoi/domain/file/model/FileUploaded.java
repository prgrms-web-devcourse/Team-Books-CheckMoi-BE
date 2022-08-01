package com.devcourse.checkmoi.domain.file.model;

import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploaded extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String extension;

    private String url;

    private Long userId;

    private String originalFileName;

    @Enumerated(EnumType.STRING)
    private Domain domain;

    private FileUploaded(Long id, String extension, String url, Long userId,
        String originalFileName, Domain domain) {
        this.id = id;
        this.extension = extension;
        this.url = url;
        this.userId = userId;
        this.originalFileName = originalFileName;
        this.domain = domain;
    }

    @Builder
    public FileUploaded(Long id, String extension, String url, Long userId,
        String originalFileName) {
        this(id, extension, url, userId, originalFileName, Domain.UNKNOWN);
    }

    public String getUrl() {
        return url;
    }

    public void changeDomain(Domain domain) {
        this.domain = domain;
    }

}
