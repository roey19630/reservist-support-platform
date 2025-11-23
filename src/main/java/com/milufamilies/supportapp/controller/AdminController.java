package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.model.Match;
import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.repository.MatchRepository;
import com.milufamilies.supportapp.repository.NeedRepository;
import com.milufamilies.supportapp.repository.OfferRepository;
import com.milufamilies.supportapp.repository.UserRepository;
import com.milufamilies.supportapp.service.NeedService;
import com.milufamilies.supportapp.service.OfferService;
import com.milufamilies.supportapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.milufamilies.supportapp.model.enums.HelpType;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final NeedService needService;
    private final OfferService offerService;
    private final MatchRepository matchRepository;
    private final NeedRepository needRepository;
    private final OfferRepository offerRepository;
    //  private final MatchRepository matchRepository;


    @GetMapping("/admin/home")
    public String showDashboard(Model model) {
        long pendingCount = userRepository.countByRoleAndApproved(Role.ROLE_FAMILY, false);
        model.addAttribute("pendingCount", pendingCount);
        return "admin/admin_dashboard";
    }

    @GetMapping("/admin/family-approvals")
    public String showPendingFamilies(Model model) {
        List<User> pendingFamilies = userRepository.findByRoleAndApproved(Role.ROLE_FAMILY, false);
        model.addAttribute("pendingFamilies", pendingFamilies);
        return "admin/family_approvals";
    }

    @PostMapping("/admin/approve/{id}")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/admin/family-approvals";
    }

    @PostMapping("/admin/reject/{id}")
    public String rejectUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/family-approvals";
    }

    @GetMapping("/admin/users")
    public String listUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String approved,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<User> users = userService.filterUsers(role, approved, keyword);
        model.addAttribute("users", users);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedApproved", approved);
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }


    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        return "admin/edit_user";
    }

    @PostMapping("/admin/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser) {
        userService.updateUserBasicInfo(id, updatedUser);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/needs")
    public String listNeeds(@RequestParam(required = false) String helpType,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String keyword,
                            Model model) {

        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("statuses", NeedStatus.values());
        model.addAttribute("selectedHelpType", helpType);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);

        List<Need> needs = needService.filterForAdmin(helpType, status, keyword);
        model.addAttribute("needs", needs);
        return "admin/needs";
    }

    @GetMapping("/admin/offers")
    public String listOffers(@RequestParam(required = false) String helpType,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String keyword,
                             Model model) {

        model.addAttribute("helpTypes", HelpType.values());
        model.addAttribute("statuses", OfferStatus.values());
        model.addAttribute("selectedHelpType", helpType);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);

        List<Offer> offers = offerService.filterForAdmin(helpType, status, keyword);
        model.addAttribute("offers", offers);
        return "admin/offers";
    }

    @GetMapping("/admin/matching")
    public String showMatchTable(Model model) {
        List<Match> matches = matchRepository.findAll();
        model.addAttribute("matches", matches);
        return "admin/matching";
    }
    @GetMapping("/admin/statistics")
    public String showStatistics(Model model) {
        // סה"כ נתונים
        model.addAttribute("totalNeeds", needRepository.count());
        model.addAttribute("totalOffers", offerRepository.count());
        model.addAttribute("totalMatches", matchRepository.count());
        model.addAttribute("matchedCount", matchRepository.countByStatus(MatchStatus.GOOD_ENDING));

        // לפי HelpType
        List<String> helpTypeLabels = Arrays.stream(HelpType.values())
                .map(Enum::name)
                .toList();
        List<Long> helpTypeCounts = helpTypeLabels.stream()
                .map(label -> needRepository.countByHelpType(HelpType.valueOf(label)))
                .toList();
        model.addAttribute("helpTypeLabels", helpTypeLabels);
        model.addAttribute("helpTypeCounts", helpTypeCounts);

        // לפי Region
        List<String> regionLabels = Arrays.stream(Region.values())
                .map(Enum::name)
                .toList();
        List<Long> regionCounts = regionLabels.stream()
                .map(label -> needRepository.countByRegion(Region.valueOf(label)))
                .toList();
        model.addAttribute("regionLabels", regionLabels);
        model.addAttribute("regionCounts", regionCounts);

        // לפי TimeSlot
        List<String> timeSlotLabels = Arrays.stream(TimeSlot.values())
                .map(Enum::name)
                .toList();
        List<Long> timeSlotCounts = timeSlotLabels.stream()
                .map(label -> offerRepository.countByTimeSlot(TimeSlot.valueOf(label)))
                .toList();
        model.addAttribute("timeSlotLabels", timeSlotLabels);
        model.addAttribute("timeSlotCounts", timeSlotCounts);

        // לפי MatchStatus
        List<String> matchStatusLabels = Arrays.stream(MatchStatus.values())
                .map(Enum::name)
                .toList();
        List<Long> matchStatusCounts = matchStatusLabels.stream()
                .map(label -> matchRepository.countByStatus(MatchStatus.valueOf(label)))
                .toList();
        model.addAttribute("matchStatusLabels", matchStatusLabels);
        model.addAttribute("matchStatusCounts", matchStatusCounts);

        return "admin/statistics";
    }


}
