package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.ChatThread;
import com.milufamilies.supportapp.model.Message;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.repository.ChatThreadRepository;
import com.milufamilies.supportapp.repository.MessageRepository;
import com.milufamilies.supportapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatThreadRepository chatThreadRepository;

    public void saveMessage(Long senderId, Long chatThreadId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow();
        ChatThread chatThread = chatThreadRepository.findById(chatThreadId).orElseThrow();

        Message message = Message.builder()
                .sender(sender)
                .chatThread(chatThread)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }

    public List<Message> getMessagesForChatThread(Long chatThreadId) {
        return messageRepository.findByChatThreadIdOrderByTimestampAsc(chatThreadId);
    }
}
