    package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.OfferFormDto;
import com.milufamilies.supportapp.model.Match;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.service.MatchService;
import com.milufamilies.supportapp.service.OfferService;
import com.milufamilies.supportapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/volunteer")
public class VolunteerPageController {

    private final OfferService offerService;
    private final UserService userService;
    private final MatchService matchService;

    @GetMapping("/new")
    public String showNewOfferForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || user.getRole() != Role.ROLE_VOLUNTEER) {
            return "redirect:/login";
        }
        int activeMatchCount = matchService.getActiveMatchesForVolunteer(user.getId()).size();

        model.addAttribute("activeMatchCount", activeMatchCount);

        model.addAttribute("offer", new OfferFormDto());
        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("regions", Region.values());
        model.addAttribute("days", DaysAvailable.values());
        model.addAttribute("times", TimeSlot.values());

        return "volunteer/new_offer";
    }

    @PostMapping
    public String submitNewOffer(@Valid @ModelAttribute("offer") OfferFormDto offerDto,
                                 BindingResult result,
                                 HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("loggedUser");


        if (user == null || user.getRole() != Role.ROLE_VOLUNTEER) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("helpTypes", HelpType.values());
            model.addAttribute("regions", Region.values());
            model.addAttribute("days", DaysAvailable.values());
            model.addAttribute("times", TimeSlot.values());

            return "volunteer/new_offer";
        }

        Offer offer = Offer.builder()
                .user(user)
                .helpType(offerDto.getHelpType())
                .description(offerDto.getDescription())
                .regions(offerDto.getRegions())
                .daysAvailable(offerDto.getDaysAvailable())
                .timeSlots(offerDto.getTimeSlots())
                .createdAt(LocalDateTime.now())
                .build();

        offerService.createOffer(offer);
        return "redirect:/volunteer/dashboard";
    }

    @GetMapping("/dashboard")
    public String showVolunteerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");


        if (user == null || user.getRole() != Role.ROLE_VOLUNTEER) {
            System.out.println("🔴 בעיה בהתחברות למתנדב:");
            System.out.println("user = " + user);
            if (user != null) {
                System.out.println("user.getRole() = " + user.getRole());
            }
            return "redirect:/login";
        }

        int activeMatchCount = matchService.countActiveMatchesForVolunteer(user.getId());


        List<Offer> myOffers = offerService.getOffersByUser(user);
        model.addAttribute("offers", myOffers);
        model.addAttribute("user", user);
        model.addAttribute("activeMatchCount", activeMatchCount);


        return "volunteer/volunteer_dashboard";

    }

    @GetMapping("matches/active")
    public String showActiveMatches(Model model, HttpSession session) {
        User volunteer = (User) session.getAttribute("loggedUser");
        List<Match> matches = matchService.getActiveMatchesForVolunteer(volunteer.getId());
        model.addAttribute("matches", matches);
        return "volunteer/active_matches";
    }


}
