package com.netology.netologycloudservice.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UserUpdateRequest {
    @Max(255)
    @NotNull
    private String username;

    @Max(255)
    @NotNull
    private String password;

    @Max(20)
    @NotNull
    private String phoneNumber;

    private Date birthdate;
}
