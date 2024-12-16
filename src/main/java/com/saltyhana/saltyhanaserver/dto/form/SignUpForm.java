package com.saltyhana.saltyhanaserver.dto.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SignUpForm {
    @NotBlank
    private String identifier;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private LocalDate birth;
}
