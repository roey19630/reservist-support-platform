package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.NeedFilterDto;
import com.milufamilies.supportapp.model.*;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.repository.ChatThreadRepository;
import com.milufamilies.supportapp.repository.UserRepository;
import com.milufamilies.supportapp.service.ChatThreadService;
import com.milufamilies.supportapp.service.MatchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final ChatThreadService chatThreadService;
    private final UserRepository userRepository;

    @GetMapping("/needs")
    public String showMatchingNeeds(@ModelAttribute NeedFilterDto filter,
                                    @RequestParam(name = "myMatches", required = false) Boolean myMatches,
                                    HttpSession session,
                                    Model model) {
        User volunteer = (User) session.getAttribute("loggedUser");
        if (volunteer == null || volunteer.getRole() != Role.ROLE_VOLUNTEER) {
            return "redirect:/login";
        }
        List<Need> needs;

        if (Boolean.TRUE.equals(myMatches)) {
            needs = matchService.findMatchingNeedsForVolunteer(volunteer);
        } else {
            boolean noFiltersSelected =
                    filter.getHelpType() == null &&
                            filter.getRegion() == null &&
                            filter.getDay() == null &&
                            filter.getTime() == null;

            needs = noFiltersSelected
                    ? matchService.findAllNeedsByStatus(NeedStatus.PENDING)
                    : matchService.filterNeeds(
                    NeedStatus.PENDING,
                    filter.getHelpType(),
                    filter.getRegion(),
                    filter.getDay(),
                    filter.getTime()
            );
        }
        Map<Long, Match> existingMatches = new HashMap<>();
        for (Need need : needs) {
            Match m = matchService.findExistingMatchByNeedAndVolunteer(need.getId(), volunteer.getId());
            if (m != null) {
                existingMatches.put(need.getId(), m);
            }
        }

        int activeMatchCount = matchService.getActiveMatchesForVolunteer(volunteer.getId()).size();
        model.addAttribute("activeMatchCount", activeMatchCount);

        model.addAttribute("existingMatches", existingMatches);

        model.addAttribute("needs", needs);
        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("regions", Region.values());
        model.addAttribute("days", DaysAvailable.values());
        model.addAttribute("times", TimeSlot.values());

        model.addAttribute("selectedHelpType", filter.getHelpType());
        model.addAttribute("selectedRegion", filter.getRegion());
        model.addAttribute("selectedDay", filter.getDay());
        model.addAttribute("selectedTime", filter.getTime());

        return "volunteer/matching_needs";
    }

    @GetMapping("/offers")
    public String showMatchingOffers(@ModelAttribute NeedFilterDto filter,
                                     @RequestParam(name = "myMatches", required = false) Boolean myMatches,
                                     HttpSession session,
                                     Model model) {
        User family = (User) session.getAttribute("loggedUser");
        if (family == null || family.getRole() != Role.ROLE_FAMILY) {
            return "redirect:/login";
        }

        List<Offer> offers;

        if (Boolean.TRUE.equals(myMatches)) {
            offers = matchService.findMatchingOffersForFamily(family);
        } else {
            boolean noFiltersSelected =
                    filter.getHelpType() == null &&
                            filter.getRegion() == null &&
                            filter.getDay() == null &&
                            filter.getTime() == null;

            offers = noFiltersSelected
                    ? matchService.findAllOffers()
                    : matchService.filterOffers(
                    filter.getHelpType(),
                    filter.getRegion(),
                    filter.getDay(),
                    filter.getTime()
            );
        }

        Map<Long, Match> existingMatches = new HashMap<>();
        for (Offer offer : offers) {
            Match m = matchService.findExistingMatch(family.getId(), offer.getId());
            if (m != null) {
                existingMatches.put(offer.getId(), m);
            }
        }
        int pendingMatchCount = matchService.countPendingMatchesForFamily(family.getId());


        Map<Long, Long> chatThreadIds = new HashMap<>();
        for (Match m : existingMatches.values()) {

            ChatThread chat = chatThreadService.getOrCreateChat(
                    userRepository.findById(m.getFamilyId()).orElseThrow(),
                    userRepository.findById(m.getVolunteerId()).orElseThrow()
            );
            chatThreadIds.put(m.getId(), chat.getId());
        }
        model.addAttribute("chatThreadIds", chatThreadIds);

        model.addAttribute("offers", offers);
        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("regions", Region.values());
        model.addAttribute("days", DaysAvailable.values());
        model.addAttribute("times", TimeSlot.values());

        model.addAttribute("selectedHelpType", filter.getHelpType());
        model.addAttribute("selectedRegion", filter.getRegion());
        model.addAttribute("selectedDay", filter.getDay());
        model.addAttribute("selectedTime", filter.getTime());

        model.addAttribute("user", family);
        model.addAttribute("existingMatches", existingMatches);
        model.addAttribute("pendingMatchCount", pendingMatchCount);

        return "families/matching_offers";
    }

    @PostMapping("/accept-direct")
    public String acceptOfferDirectly(@RequestParam Long offerId, HttpSession session, RedirectAttributes redirectAttributes) {
        User family = (User) session.getAttribute("loggedUser");
        if (family == null || family.getRole() != Role.ROLE_FAMILY) {
            return "redirect:/login";
        }
        try {
            matchService.createMatchFromOffer(offerId, family);
            redirectAttributes.addFlashAttribute("success", "ההתאמה נוצרה! נפתח צ'אט עם המתנדב.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "לא נמצאה בקשה פתוחה למשפחה.");
        }
        return "redirect:/match/offers";

    }



    @PostMapping("/request")
    public String requestMatch(@RequestParam Long needId, HttpSession session, RedirectAttributes redirectAttributes) {
        User volunteer = (User) session.getAttribute("loggedUser");
        if (volunteer == null || volunteer.getRole() != Role.ROLE_VOLUNTEER) {
            return "redirect:/login";
        }

        try {
            matchService.createMatchRequest(needId, volunteer);
            redirectAttributes.addFlashAttribute("success", "נשלחה בקשת התאמה למשפחה!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "לא ניתן היה לשלוח בקשה.");
        }

        return "redirect:/match/needs";
    }


    @GetMapping("/admin/matching")
    public String showAllMatches(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ROLE_ADMIN) {
            return "redirect:/login";
        }

        List<com.milufamilies.supportapp.model.Match> matches = matchService.getAllMatches();
        model.addAttribute("matches", matches);
        return "admin/matching";
    }


}
