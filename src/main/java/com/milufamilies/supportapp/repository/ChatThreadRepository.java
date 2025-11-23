package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.ChatThread;
import com.milufamilies.supportapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    Optional<ChatThread> findByFamilyAndVolunteer(User family, User volunteer);

    Optional<ChatThread> findById(Long id);

    void deleteByFamilyAndVolunteer(User family, User volunteer);

    List<ChatThread> findByFamily(User family);

    List<ChatThread> findByVolunteer(User volunteer);
}
