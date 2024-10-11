package com.netology.netologycloudservice.controller;

import com.netology.netologycloudservice.configuration.JwtTokenProvider;
import com.netology.netologycloudservice.dao.CloudUser;
import com.netology.netologycloudservice.dao.Role;
import com.netology.netologycloudservice.repository.CloudUserRepository;
import com.netology.netologycloudservice.request.AuthRequest;
import com.netology.netologycloudservice.request.AuthResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CloudUserRepository userRepository;

    @Test
    void loginTest() throws Exception {
        // Мокируем данные пользователя
        CloudUser mockUser = CloudUser.builder()
                .username("testUser")
                .email("test@mail.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        // Мокируем успешную аутентификацию через AuthenticationManager
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Мокируем генерацию токена после успешной аутентификации
        Mockito.when(jwtTokenProvider.generateToken(mockUser.getEmail(), mockUser.getRole()))
                .thenReturn("testToken");

        // Выполняем запрос на /auth/login с email и паролем
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@mail.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));  // Проверяем, что вернулся правильный токен
    }

    @Test
    void loginInvalidCredentialsTest() throws Exception {
        // Мокируем ошибочную аутентификацию
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Выполняем запрос на /auth/login с неверными данными
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"wrong@mail.com\", \"password\": \"wrongPassword\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));  // Проверяем, что вернулась ошибка
    }
}