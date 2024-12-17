package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.UserDTO;
import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.entity.User;


public class UserMapper {
    public static User toEntity(SignUpForm signUpForm, String password) {
        return User.builder()
                .email(signUpForm.getEmail())
                .name(signUpForm.getName())
                .identifier(signUpForm.getIdentifier())
                .password(password)
                .birth(signUpForm.getBirth())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .identifier(userDTO.getIdentifier())
                .password(userDTO.getPassword())
                .birth(userDTO.getBirth())
                .profileImage(userDTO.getProfileImage())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .identifier(user.getIdentifier())
                .password(user.getPassword())
                .birth(user.getBirth())
                .profileImage(user.getProfileImage())
                .build();
    }
}
