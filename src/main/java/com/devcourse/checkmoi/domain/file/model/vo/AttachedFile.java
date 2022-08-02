package com.devcourse.checkmoi.domain.file.model.vo;

import static org.apache.commons.io.FilenameUtils.getExtension;
import com.devcourse.checkmoi.domain.file.exception.NotAllowedFileException;
import java.io.IOException;
import java.util.UUID;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


public class AttachedFile {

    private final String originalFileName;

    private final String contentType;

    private final byte[] bytes;

    public AttachedFile(String originalFileName, String contentType, byte[] bytes) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    private static void checkMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() <= 0
            || multipartFile.getOriginalFilename() == null) {
            throw new NotAllowedFileException();
        }
    }

    private static void checkContentType(MultipartFile multipartFile, FileType fileType) {
        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType) || !contentType.toLowerCase()
            .startsWith(fileType.getPrefix())) {
            throw new NotAllowedFileException();
        }
    }

    public static AttachedFile toAttachedFile(MultipartFile multipartFile,
        FileType fileType) {

        try {
            checkMultipartFile(multipartFile);
            checkContentType(multipartFile, fileType);

            return new AttachedFile(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String randomName(String basePath, String defaultExtension) {
        String name = ObjectUtils.isEmpty(basePath) ? UUID.randomUUID().toString()
            : basePath + "/" + UUID.randomUUID();
        return name + "." + extension(defaultExtension);
    }

    public String extension(String defaultExtension) {
        String extractedExtension = getExtension(originalFileName);

        if (ObjectUtils.isEmpty(extractedExtension)) {
            return defaultExtension;
        }
        return extractedExtension;
    }

    public enum FileType {
        IMAGE("image"),
        TEXT("text");

        private final String prefix;

        FileType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
