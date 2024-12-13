package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDTO {
    private String userId;
    private String password;
    private String userName;
    private String birth;
    private String email;
}
