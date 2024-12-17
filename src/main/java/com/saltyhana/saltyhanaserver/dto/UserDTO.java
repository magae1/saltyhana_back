package com.saltyhana.saltyhanaserver.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDTO {
    private Long id;
    private String identifier;
    private String password;
    private String name;
    private String email;
    private String profileImage;
    private LocalDate birth;
}
