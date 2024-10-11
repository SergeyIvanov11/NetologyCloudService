package com.netology.netologycloudservice.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class CloudUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "email", length = 50, unique = true, nullable = false)
    String email;

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    String phoneNumber;

    @Column(name = "birthdate", nullable = true)
    Date birthdate; // дата рождения пользователя

    @Column(name = "registration_date", nullable = false)
    LocalDateTime registrationDate; // дата регистрации

    @Column(name = "avatar", nullable = true)
    byte[] avatar; // файл с аватаркой

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Transient
    List<Long> uploadedFiles; // файлы пользователя - список id файлов

    public void addFileToUsersUploadedFiles(Long id) {
        if (uploadedFiles == null) {
            uploadedFiles = new ArrayList<>();
        }
        this.uploadedFiles.add(id);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Преобразуем роль в объект GrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Возвращает true, если аккаунт не истек
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Возвращает true, если аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Возвращает true, если учетные данные не истекли
    }

    @Override
    public boolean isEnabled() {
        return true;  // Возвращает true, если аккаунт активен
    }
}
