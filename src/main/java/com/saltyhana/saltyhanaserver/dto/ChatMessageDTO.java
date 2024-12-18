package com.saltyhana.saltyhanaserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private String name;
    private String message;
    private LocalDateTime timestamp;
    private String sender; // 클라이언트와 서버 구분
}