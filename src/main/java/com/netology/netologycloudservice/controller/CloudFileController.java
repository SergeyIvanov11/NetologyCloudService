package com.netology.netologycloudservice.controller;

import com.netology.netologycloudservice.dao.CloudFile;
import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.request.FileRequest;
import com.netology.netologycloudservice.service.CloudFileService;
import com.netology.netologycloudservice.service.CloudUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/cloud")
@RestController
@RequiredArgsConstructor
public class CloudFileController {
    private final CloudFileService fileService;
    private final CloudUserService userService;

    // Загрузка файла
    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(Authentication auth, @Valid @RequestBody FileRequest fileRequest) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            CloudFile file = fileService.createFile(user, fileRequest);
            userService.addFileToUsersUploadedFiles(user.getId(), file.getId());
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Получение файла по ID
    @GetMapping("/{id}")
    public ResponseEntity<CloudFile> findById(Authentication auth, @PathVariable Long id) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            CloudFile file = fileService.findByIdAndUser(id, user.getId());  // Проверка, что файл принадлежит пользователю
            return ResponseEntity.ok(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Получение списка файлов пользователя
    @GetMapping("/list")
    public ResponseEntity<List<CloudFile>> getFiles(Authentication auth) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            List<CloudFile> files = fileService.getFilesByUser(user.getId());
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Удаление файла
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(Authentication auth, @PathVariable Long id) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            fileService.deleteFileForUser(id, user.getId());  // Проверяем, что файл принадлежит пользователю перед удалением
            return ResponseEntity.ok("File with id " + id + " has been successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
        }
    }
}