package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.ChatMessageDto;
import com.milufamilies.supportapp.model.ChatThread;
import com.milufamilies.supportapp.model.Message;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.repository.ChatThreadRepository;
import com.milufamilies.supportapp.repository.UserRepository;
import com.milufamilies.supportapp.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ChatThreadRepository chatThreadRepository;

    @GetMapping("/{chatThreadId}")
    public String chatPage(@PathVariable Long chatThreadId, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        ChatThread chatThread = chatThreadRepository.findById(chatThreadId)
                .orElseThrow(() -> new IllegalArgumentException("ChatThread not found"));

        // הגנה מפני נתונים חסרים
        if (chatThread.getFamily() == null || chatThread.getVolunteer() == null) {
            model.addAttribute("error", "השיחה לא תקינה");
            return "chat";
        }

        List<Message> messages = messageService.getMessagesForChatThread(chatThreadId);
        model.addAttribute("messages", messages);
        model.addAttribute("chatThreadId", chatThreadId);
        model.addAttribute("currentUser", currentUser);

        User otherUser = chatThread.getFamily().getId().equals(currentUser.getId())
                ? chatThread.getVolunteer()
                : chatThread.getFamily();

        model.addAttribute("otherUser", otherUser);

        return "chat";
    }


    @PostMapping("/send")
    public String sendMessage(@ModelAttribute ChatMessageDto dto) {
        messageService.saveMessage(dto.getSenderId(), dto.getChatThreadId(), dto.getContent());
        return "redirect:/chat/" + dto.getChatThreadId();
    }

    @GetMapping("/poll/{chatThreadId}")
    @ResponseBody
    public List<Message> pollMessages(@PathVariable Long chatThreadId) {
        return messageService.getMessagesForChatThread(chatThreadId);
    }

    @GetMapping("/list")
    public String chatListPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<ChatThread> chatThreads = currentUser.getRole().name().equals("ROLE_FAMILY")
                ? chatThreadRepository.findByFamily(currentUser)
                : chatThreadRepository.findByVolunteer(currentUser);

        Map<Long, User> usersMap = new HashMap<>();


        for (ChatThread thread : chatThreads) {
            User otherUser = thread.getFamily().getId().equals(currentUser.getId())
                    ? thread.getVolunteer()
                    : thread.getFamily();
            usersMap.put(thread.getId(), otherUser);
        }

        model.addAttribute("chatThreads", chatThreads);
        model.addAttribute("usersMap", usersMap);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("role", currentUser.getRole());
        model.addAttribute("page", "chat");
        model.addAttribute("user", currentUser);



        return "chat_list";
    }

}
