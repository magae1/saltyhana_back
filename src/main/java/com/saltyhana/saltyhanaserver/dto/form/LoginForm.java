package com.saltyhana.saltyhanaserver.dto.form;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginForm {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;
}
