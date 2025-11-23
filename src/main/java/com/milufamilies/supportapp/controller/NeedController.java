package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.NeedRequestDto;
import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.service.MatchService;
import com.milufamilies.supportapp.service.NeedService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/needs")
@RequiredArgsConstructor
public class NeedController {

    private final NeedService needService;
    private final MatchService matchService;

    @GetMapping("/new")
    public String showNewNeedForm(Model model, HttpSession session) {
        User family = (User) session.getAttribute("loggedUser");
        int pendingMatchCount = matchService.countPendingMatchesForFamily(family.getId());

        model.addAttribute("pendingMatchCount", pendingMatchCount);
        model.addAttribute("needRequestDto", new NeedRequestDto());
        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("regions", Region.values());
        model.addAttribute("days", DaysAvailable.values());
        model.addAttribute("times", TimeSlot.values());
        model.addAttribute("user", family);

        return "families/new_need"; // טופס HTML
    }

    @PostMapping
    public String submitNeed(
            @Valid @ModelAttribute("needRequestDto") NeedRequestDto dto,
            BindingResult result,
            HttpSession session,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("helpTypes", HelpType.values());
            model.addAttribute("regions", Region.values());
            model.addAttribute("days", DaysAvailable.values());
            model.addAttribute("times", TimeSlot.values());
            return "families/new_need"; // החזרה לטופס עם שגיאות
        }
        User user = (User) session.getAttribute("loggedUser");

        Need need = Need.builder()
                .user(user)
                .helpType(dto.getHelpType())
                .regions(dto.getRegions())
                .description(dto.getDescription())
                .preferredResponseTime(dto.getPreferredResponseTime())
                .daysAvailable(dto.getDaysAvailable())
                .timeSlots(dto.getTimeSlots())
                .status(NeedStatus.PENDING)
                .build();

        needService.saveNeed(need);
        return "redirect:/family/dashboard"; // לאחר השליחה
    }
}
