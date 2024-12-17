package com.saltyhana.saltyhanaserver.dto;


import lombok.Getter;


@Getter
public class MessageDTO {
    private String name;

    public MessageDTO() {}

    public MessageDTO(String name) {
        this.name = name;
    }
}
