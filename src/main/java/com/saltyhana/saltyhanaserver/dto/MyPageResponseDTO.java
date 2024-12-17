package com.saltyhana.saltyhanaserver.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MyPageResponseDTO {
    private String email;
    private String identifier;
    private String name;
    private LocalDate birth;
    private String profileImg;
}
