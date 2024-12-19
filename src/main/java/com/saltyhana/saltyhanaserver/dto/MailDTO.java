package com.saltyhana.saltyhanaserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {
    private String from;
    private String to;
    private String title;
    private String message;
}