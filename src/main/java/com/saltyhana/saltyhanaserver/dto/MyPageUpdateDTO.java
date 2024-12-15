package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MyPageUpdateDTO {
    private String email;
    private String id;
    private String name;
    private Date birth;
    private String password;
    private String profileImg;
}