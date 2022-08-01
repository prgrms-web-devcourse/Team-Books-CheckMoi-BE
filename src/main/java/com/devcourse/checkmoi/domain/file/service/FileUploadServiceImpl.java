package com.devcourse.checkmoi.domain.file.service;

import com.devcourse.checkmoi.domain.file.dto.AttachedFileRequest;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse.Upload;
import com.devcourse.checkmoi.domain.file.model.FileUploaded;
import com.devcourse.checkmoi.domain.file.model.vo.AttachedFile;
import com.devcourse.checkmoi.domain.file.model.vo.AttachedFile.FileType;
import com.devcourse.checkmoi.domain.file.repository.FileUploadedRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final S3Upload s3Upload;

    private final FileUploadedRepository repository;

    public FileUploadServiceImpl(S3Upload s3Upload,
        FileUploadedRepository repository) {
        this.s3Upload = s3Upload;
        this.repository = repository;
    }

    @Override
    public AttachedFileResponse.Upload upload(AttachedFileRequest.Upload request, Long userId) {
        List<String> urls = new ArrayList<>();

        request.files().stream()
            .map(multipartFile -> AttachedFile.toAttachedFile(multipartFile, FileType.IMAGE))
            .forEach(file -> // TODO : AttachedFile 로직으로 인한 NPE 발생 가능성
                save(file, urls, userId));

        return new Upload(urls);
    }

    @Override
    public void delete(String url) {
        repository.findByUrl(url)
            .ifPresent(this::remove);
    }

    private void remove(FileUploaded file) {
        s3Upload.delete(file.getUrl());
        repository.delete(file);
    }

    private void save(AttachedFile file, List<String> urls, Long userId) {
        String url = s3Upload.upload(file.getBytes(),
            file.randomName(null, "jpeg"),
            file.getContentType(),
            null);

        FileUploaded fileUploaded = FileUploaded.builder()
            .extension(file.extension("jpeg"))
            .originalFileName(file.getOriginalFileName())
            .userId(userId)
            .url(url)
            .build();

        urls.add(url);
        repository.save(fileUploaded);
    }
}
