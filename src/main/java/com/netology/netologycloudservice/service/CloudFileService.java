package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.dao.CloudFile;
import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.repository.CloudFileRepository;
import com.netology.netologycloudservice.request.FileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudFileService {
    private final CloudFileRepository repository;

    public List<CloudFile> getFilesByUser(Long userId) {
        return repository.findByOwner(userId);
    }

    public CloudFile findByIdAndUser(Long fileId, Long userId) {
        return repository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
    }
    public CloudFile createFile(CloudUser user, FileRequest fileRequest) {
        CloudFile file = new CloudFile();
        file.setFilename(fileRequest.getFilename());
        file.setOwner(user);
        file.setFilePath(fileRequest.getFilePath());
        file.setDateOfUpload(LocalDateTime.now());
        return repository.save(file);
    }

    public void deleteFileForUser(Long fileId, Long userId) {
        CloudFile file = repository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new IllegalArgumentException("File not found or access denied"));
        repository.deleteById(fileId);
    }
}
