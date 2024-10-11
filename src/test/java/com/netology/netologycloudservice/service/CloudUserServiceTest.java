package com.netology.netologycloudservice.service;

import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.dao.Role;
import com.netology.netologycloudservice.repository.CloudUserRepository;
import com.netology.netologycloudservice.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CloudUserServiceTest {
    @Autowired
    private CloudUserService userService;
    @MockBean
    private CloudUserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void createUserTest() {
        UserRequest request = new UserRequest();
        request.setUsername("testUser");
        request.setEmail("test@mail.com");
        request.setPassword("password");
        request.setPhoneNumber("1234567890");

        CloudUser user = CloudUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        // Мокируем PasswordEncoder
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Мокируем репозиторий для сохранения пользователя
        Mockito.when(userRepository.save(Mockito.any(CloudUser.class))).thenReturn(user);

        // Вызов метода сервиса
        CloudUser createdUser = userService.createUser(request);

        // Проверки
        assertNotNull(createdUser);  // Убеждаемся, что пользователь не null
        assertEquals("testUser", createdUser.getUsername());  // Проверяем, что username совпадает
        assertEquals("test@mail.com", createdUser.getEmail());
        assertEquals("1234567890", createdUser.getPhoneNumber());
        verify(userRepository, times(1)).save(Mockito.any(CloudUser.class)); // Проверяем, что вызов был 1 раз
    }

    @Test
    void findByEmailTest() {
        String email = "test@mail.com";
        CloudUser user = CloudUser.builder().email(email).build();

        // Мокаем репозиторий для поиска по email
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Вызов метода сервиса
        CloudUser foundUser = userService.findByEmail(email);

        // Проверки
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void findByUsernameTest() {
        // Подготовка данных
        CloudUser user = CloudUser.builder()
                .username("testUser")
                .email("test@mail.com")
                .phoneNumber("1234567890")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Вызов метода
        CloudUser foundUser = userService.findByUsername("testUser");

        // Проверки
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("test@mail.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void deleteUserTest() {
        Long userId = 1L;
        // Вызов метода сервиса
        userService.deleteUser(userId);
        // Проверяем, что метод удаления был вызван один раз
        verify(userRepository, times(1)).deleteById(userId);
    }
}