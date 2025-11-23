package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.ChatThread;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.repository.ChatThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatThreadService {

    private final ChatThreadRepository chatThreadRepository;

    public Optional<ChatThread> getChatBetween(User family, User volunteer) {
        return chatThreadRepository.findByFamilyAndVolunteer(family, volunteer);
    }

    public ChatThread getOrCreateChat(User family, User volunteer) {
        if (family == null || volunteer == null) {
            throw new IllegalArgumentException("Both family and volunteer must be non-null");
        }

        return chatThreadRepository.findByFamilyAndVolunteer(family, volunteer)
                .orElseGet(() -> {
                    ChatThread chat = ChatThread.builder()
                            .family(family)
                            .volunteer(volunteer)
                            .build();
                    return chatThreadRepository.save(chat);
                });
    }

    public void deleteChat(User family, User volunteer) {
        chatThreadRepository.deleteByFamilyAndVolunteer(family, volunteer);
    }

    public ChatThread getById(Long id) {
        return chatThreadRepository.findById(id).orElse(null);
    }
}
