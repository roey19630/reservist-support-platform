package com.milufamilies.supportapp.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
    private Long chatThreadId;
    private Long senderId;
    private String content;
}
