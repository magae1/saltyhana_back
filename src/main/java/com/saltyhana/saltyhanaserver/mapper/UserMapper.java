package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.UserDTO;
import com.saltyhana.saltyhanaserver.dto.form.MyPageUpdateForm;
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

    public static User toEntity(Long id, MyPageUpdateForm myPageUpdateForm) {
        User.UserBuilder builder = User.builder();
        builder.id(id);
        if (myPageUpdateForm.getEmail() != null) {
            builder.email(myPageUpdateForm.getEmail());
        }
        if (myPageUpdateForm.getName() != null) {
            builder.name(myPageUpdateForm.getName());
        }
        if (myPageUpdateForm.getIdentifier() != null) {
            builder.identifier(myPageUpdateForm.getIdentifier());
        }
        if (myPageUpdateForm.getBirth() != null) {
            builder.birth(myPageUpdateForm.getBirth());
        }
        if (myPageUpdateForm.getPassword() != null) {
            builder.password(myPageUpdateForm.getPassword());
        }
        return builder.build();
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

    public static MyPageResponseDTO toMyPageResponseDTO(User user) {
        return MyPageResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .identifier(user.getIdentifier())
                .birth(user.getBirth())
                .profileImg(user.getProfileImage())
                .build();
    }
}
