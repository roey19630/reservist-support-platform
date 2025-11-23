package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.service.MatchService;
import com.milufamilies.supportapp.service.NeedService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.milufamilies.supportapp.service.UserService;

@Controller
@RequiredArgsConstructor
public class FamilyController {

    private final NeedService needService;
    private final UserService userService;
    private final MatchService matchService;

    @GetMapping("/family/dashboard")
    public String familyDashboard(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("loggedUser");

        if (sessionUser == null || sessionUser.getRole() != Role.ROLE_FAMILY) {
            return "redirect:/login";
        }

        // שליפה מחדש מה־DB לפי ה-ID (הכי חשוב!)
        User user = userService.findById(sessionUser.getId());

        if (!user.isApproved()) {
            return "families/awaiting_approval";
        }
        int pendingMatchCount = matchService.countPendingMatchesForFamily(user.getId());

        List<Need> needs = needService.getNeedsByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("needs", needs);
        model.addAttribute("page", "home");
        model.addAttribute("pendingMatchCount", pendingMatchCount);

        return "families/family_dashboard";
    }

    @GetMapping("/families/awaiting-approval")
    public String awaitingApproval() {
        return "families/awaiting_approval";
    }
}

