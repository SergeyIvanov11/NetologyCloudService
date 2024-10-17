package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.dao.CloudFile;
import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.exception.FileNotFoundException;
import com.netology.netologycloudservice.repository.CloudFileRepository;
import com.netology.netologycloudservice.request.FileRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudFileService {
    private final CloudFileRepository repository;
    private final Logger logger;

    public List<CloudFile> getFilesByUser(Long userId) {
        return repository.findByOwner(userId);
    }

    public CloudFile findByIdAndUser(Long fileId, Long userId) {
        return repository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new FileNotFoundException("File not found or access denied"));
    }
    @Transactional
    public CloudFile createFile(CloudUser user, FileRequest fileRequest) {
        if (repository.findByOwnerAndFilename(user.getId(), fileRequest.getFilename()).isPresent()) {
            logger.error("A file with this name already exists");
            throw new IllegalArgumentException("A file with this name already exists");
        }
        CloudFile file = new CloudFile();
        file.setFilename(fileRequest.getFilename());
        file.setOwner(user);
        file.setFilePath(fileRequest.getFilePath());
        file.setContent(fileRequest.getContent());
        file.setDateOfUpload(LocalDateTime.now());
        logger.info("New file is created");
        return repository.save(file);
    }
    @Transactional
    public void deleteFileForUser(Long fileId, Long userId) {
        CloudFile file = repository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new FileNotFoundException("File not found or access denied"));
        repository.deleteById(fileId);
        logger.info("File " + fileId + " is deleted");
    }
}
