package com.netology.netologycloudservice.controller;

import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.request.UserRequest;
import com.netology.netologycloudservice.request.UserUpdateRequest;
import com.netology.netologycloudservice.service.CloudUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class CloudUserController {

    private final CloudUserService userService;

    // Регистрация нового пользователя
    @PostMapping()
    public ResponseEntity<String> registration(@Valid @RequestBody UserRequest userRequest, @RequestParam(value = "avatar", required = false) MultipartFile file) {
        try {
            CloudUser newUser = userService.createUser(userRequest);
            if (file != null && !file.isEmpty()) {
                userService.changeAvatar(newUser.getId(), file);
            }
            return ResponseEntity.ok("New user created and saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Получение информации о текущем пользователе
    @GetMapping("/me")
    public ResponseEntity<CloudUser> getCurrentUser(Authentication auth) {
        CloudUser user = userService.findByUsername(auth.getName());
        return ResponseEntity.ok(user);
    }

    // Редактирование профиля
    @PatchMapping("/me")
    public ResponseEntity<String> updateUserProfile(Authentication auth, @Valid @RequestBody UserUpdateRequest userUpdateRequest, @RequestParam(value = "avatar", required = false) MultipartFile file) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            userService.updateUserProfile(user.getId(), userUpdateRequest);
            if (file != null && !file.isEmpty()) {
                userService.changeAvatar(user.getId(), file);
            }
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile: " + e.getMessage());
        }
    }

    // Удаление профиля
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserProfile(Authentication auth) {
        try {
            CloudUser user = userService.findByUsername(auth.getName());
            userService.deleteUser(user.getId());
            return ResponseEntity.ok("Profile deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete profile: " + e.getMessage());
        }
    }
}
