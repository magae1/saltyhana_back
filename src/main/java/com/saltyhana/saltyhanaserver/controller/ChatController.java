package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long id) {
        ChatMessageDTO test = new ChatMessageDTO(System.currentTimeMillis(), "test", "무엇이든 물어보세요!", LocalDateTime.now(), "server");
        return ResponseEntity.ok().body(List.of(test));
    }

    // STOMP 메시지 수신 처리
    @MessageMapping("/message")
    public void receiveMessage(@Payload ChatMessageDTO chat) {
        // 수신된 메시지 확인
        System.out.println("Received message: " + chat);

        // 타임스탬프 추가
        chat.setSender("client");
        chat.setTimestamp(LocalDateTime.now());

        // 메시지 전송
        template.convertAndSend("/sub/chatroom/1", chat);
    }

    // STOMP 메시지 송신 처리
    @PostMapping("/send")
    public ResponseEntity<String> sendMessageFromServer(@RequestBody ChatMessageDTO chat) {
        chat.setId(System.currentTimeMillis());
        chat.setTimestamp(LocalDateTime.now());  // 현재 시간 설정
        chat.setSender("server");                // 서버 메시지로 설정
        template.convertAndSend("/sub/chatroom/1", chat);
        return ResponseEntity.ok("Message sent successfully!");
    }
}