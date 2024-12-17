package com.saltyhana.saltyhanaserver.dto.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MyPageUpdateForm {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String identifier;

    private String password;

    private String confirmPassword;

    @NotBlank
    private String name;

    @NotBlank
    private LocalDate birth;
}
