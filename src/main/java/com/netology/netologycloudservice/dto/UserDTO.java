package com.netology.netologycloudservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDTO {

    private String username;

    private String email;

    private Date birthdate;

    private LocalDateTime registrationDate;

    private byte[] avatar;
}
