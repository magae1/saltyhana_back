package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String userId;
    private String password;
}
