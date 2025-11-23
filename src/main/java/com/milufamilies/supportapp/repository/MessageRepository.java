package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.Message;
import com.milufamilies.supportapp.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatThreadIdOrderByTimestampAsc(Long chatThreadId);
}
