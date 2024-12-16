package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.entity.User;

public class AuthMapper {
    public static User toEntity(SignUpForm signUpForm, String password) {
        return User.builder()
                .email(signUpForm.getEmail())
                .name(signUpForm.getName())
                .identifier(signUpForm.getIdentifier())
                .password(password)
                .birth(signUpForm.getBirth())
                .build();
    }
}
