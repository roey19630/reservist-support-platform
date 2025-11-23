package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.model.Match;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.MatchStatus;
import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.service.MatchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyMatchController {

    private final MatchService matchService;

    @GetMapping("/matches")
    public String showPendingMatches(HttpSession session, Model model) {
        User family = (User) session.getAttribute("loggedUser");
        if (family == null || family.getRole() != Role.ROLE_FAMILY) {
            return "redirect:/login";
        }

        int pendingMatchCount = matchService.countPendingMatchesForFamily(family.getId());
        List<Match> matches = matchService.getPendingMatchesForFamily(family);

        model.addAttribute("matches", matches);
        model.addAttribute("user", family);
        model.addAttribute("pendingMatchCount", pendingMatchCount);

        return "families/pending_matches";
    }

    @PostMapping("/matches/update")
    public String updateMatchStatus(@RequestParam Long matchId,
                                    @RequestParam MatchStatus action,
                                    RedirectAttributes redirectAttributes) {
        matchService.updateMatchStatus(matchId, action);
        redirectAttributes.addFlashAttribute("success", "הסטטוס עודכן בהצלחה");
        return "redirect:/family/matches";
    }
}
