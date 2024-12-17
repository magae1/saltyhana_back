package com.saltyhana.saltyhanaserver.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.saltyhana.saltyhanaserver.dto.ChatMessageDTO;
import com.saltyhana.saltyhanaserver.dto.MessageDTO;


@Log4j2
@Controller
public class ChatController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMessageDTO broadcast(MessageDTO messageDTO) {
        log.info(messageDTO);
        return new ChatMessageDTO("Hello, " + HtmlUtils.htmlEscape(messageDTO.getName()) + "!");
    }
}
