package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.dao.CloudFile;
import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.dao.Role;
import com.netology.netologycloudservice.repository.CloudFileRepository;
import com.netology.netologycloudservice.repository.CloudUserRepository;
import com.netology.netologycloudservice.request.FileRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CloudFileServiceTest {
    @Autowired
    private CloudFileService fileService;

    @MockBean
    private CloudFileRepository fileRepository;

    @MockBean
    private CloudUserRepository userRepository;

    @Test
    void createFileTest() {
        CloudUser user = CloudUser.builder()
                .id(1L)
                .username("testUser")
                .email("test@mail.com")
                .phoneNumber("1234567890")
                .role(Role.ROLE_USER)
                .build();

        FileRequest fileRequest = new FileRequest();
        fileRequest.setFilename("testFile");
        fileRequest.setFilePath("/path/to/file");

        CloudFile file = CloudFile.builder()
                .filename(fileRequest.getFilename())
                .filePath(fileRequest.getFilePath())
                .owner(user)
                .build();

        // Мокаем возврат пользователя
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // Мокаем сохранение файла
        Mockito.when(fileRepository.save(Mockito.any(CloudFile.class))).thenReturn(file);

        // Act
        CloudFile createdFile = fileService.createFile(user, fileRequest);

        // Assert
        assertNotNull(createdFile);
        assertEquals("testFile", createdFile.getFilename());
        assertEquals("/path/to/file", createdFile.getFilePath());
        assertEquals(user, createdFile.getOwner());

        // Проверка на вызов save
        verify(fileRepository, times(1)).save(Mockito.any(CloudFile.class));
    }

    @Test
    void getFilesByUserTest() {
        Long userId = 1L;
        List<CloudFile> files = Arrays.asList(
                CloudFile.builder().filename("file1").build(),
                CloudFile.builder().filename("file2").build()
        );

        // Мокаем возвращение списка файлов по пользователю
        Mockito.when(fileRepository.findByOwner(userId)).thenReturn(files);

        // Вызов метода сервиса
        List<CloudFile> resultFiles = fileService.getFilesByUser(userId);

        // Проверки
        assertNotNull(resultFiles);
        assertEquals(2, resultFiles.size());
        verify(fileRepository, times(1)).findByOwner(userId);
    }

    @Test
    void deleteFileForUserTest() {
        Long fileId = 1L; // ID файла для удаления
        Long userId = 1L; // ID пользователя, которому принадлежит файл

        CloudUser user = CloudUser.builder()
                .id(userId)
                .username("testUser")
                .email("test@mail.com")
                .phoneNumber("1234567890")
                .role(Role.ROLE_USER)
                .build();

        CloudFile file = CloudFile.builder()
                .id(fileId)
                .filename("testFile")
                .filePath("/path/to/file")
                .owner(user)
                .build();

        Mockito.when(fileRepository.findByIdAndUserId(fileId, userId)).thenReturn(Optional.of(file));
        fileService.deleteFileForUser(fileId, userId);
        verify(fileRepository, times(1)).deleteById(fileId);
    }

}