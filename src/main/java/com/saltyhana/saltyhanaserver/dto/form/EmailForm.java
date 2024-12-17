package com.saltyhana.saltyhanaserver.dto.form;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailForm {
    @Email
    @NotBlank
    private String email;
}

