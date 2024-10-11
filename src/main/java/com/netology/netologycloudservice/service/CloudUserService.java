package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.dao.Role;
import com.netology.netologycloudservice.dto.UserDTO;
import com.netology.netologycloudservice.exception.IncorrectFileFormatException;
import com.netology.netologycloudservice.exception.UserNotFoundException;
import com.netology.netologycloudservice.repository.CloudUserRepository;
import com.netology.netologycloudservice.request.UserRequest;
import com.netology.netologycloudservice.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CloudUserService {
    private final CloudUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final CloudFileService fileService;
    private ModelMapper mapper;

    private UserDTO convertToUserDto(CloudUser user) {
        return mapper.map(user, UserDTO.class);
    }

    public CloudUser save(CloudUser user) {
        return repository.save(user);
    }

    public UserDTO getUser(Long id) {
        return convertToUserDto(findById(id));
    }

    public CloudUser findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public CloudUser findByUsername(String name) {
        return repository
                .findByUsername(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + name));
    }

    public CloudUser findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public ResponseEntity<String> changeAvatar(Long id, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            if (file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png")) {
                CloudUser user = findById(id);
                user.setAvatar(file.getBytes());
                repository.save(user);
                return ResponseEntity.ok("Avatar for user has been successfully updated");
            } else {
                throw new IncorrectFileFormatException("Incorrect File Format");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Transactional
    public CloudUser updateUserProfile(Long id, UserUpdateRequest userUpdateRequest) {
        CloudUser user = findById(id);
        mapper.map(userUpdateRequest, user);  // обновление данных пользователя
        return repository.save(user);
    }

    @Transactional
    public CloudUser createUser(UserRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserNotFoundException("User with that username already exists");
        }
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserNotFoundException("User with that email already exists");
        }
        if (repository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserNotFoundException("User with that phonenumber already exists");
        }
        CloudUser newUser = CloudUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .birthdate(request.getBirthdate())
                .registrationDate(LocalDateTime.now())
                .role(Role.ROLE_USER)
                .build();
        return repository.save(newUser);
    }

    @Transactional
    public void addFileToUsersUploadedFiles(Long fileAuthorId, Long id) {
        CloudUser user = findById(fileAuthorId);
        user.addFileToUsersUploadedFiles(id);
        repository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
}
