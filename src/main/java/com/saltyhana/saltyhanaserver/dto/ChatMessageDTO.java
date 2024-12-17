package com.saltyhana.saltyhanaserver.dto;


import lombok.Getter;


@Getter
public class ChatMessageDTO {
    private String content;

    public ChatMessageDTO() {}

    public ChatMessageDTO(String content) {
        this.content = content;
    }
}
