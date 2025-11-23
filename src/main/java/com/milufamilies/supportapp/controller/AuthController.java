package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.RegisterFamilyDto;
import com.milufamilies.supportapp.dto.RegisterVolunteerDto;
import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.security.UserDetailsServiceImpl;
import com.milufamilies.supportapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.milufamilies.supportapp.model.FamilyDetails;
import com.milufamilies.supportapp.repository.FamilyDetailsRepository;
import com.milufamilies.supportapp.model.enums.SoldierRelation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.milufamilies.supportapp.util.ImageUtils;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final FamilyDetailsRepository familyDetailsRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ImageUtils imageUtils;

    @PostMapping("/register/family")
    public String registerFamily(@Valid @ModelAttribute("registerFamilyDto") RegisterFamilyDto dto,
                                 BindingResult result,
                                 HttpSession session,
                                 Model model) {

        if (userService.emailExists(dto.getEmail())) {
            result.rejectValue("email", null, "אימייל זה כבר קיים במערכת");
        }
        if (userService.idNumberExists(dto.getIdNumber())) {
            result.rejectValue("idNumber", null, "תעודת זהות זו כבר רשומה במערכת");
        }

        if (result.hasErrors()) {
            model.addAttribute("relations", SoldierRelation.values());
            return "/families/register_family";
        }

        // יצירת יוזר
        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword()) // 🔒 חשוב מאוד להצפין בעתיד!
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .idNumber(dto.getIdNumber())
                .approved(false) // משפחות ממתינות לאישור
                .role(Role.ROLE_FAMILY)
                .build();

        userService.saveUser(user);

        // שמירת soldierName ו-soldierRelation
        FamilyDetails familyDetails = FamilyDetails.builder()
                .user(user)
                .soldierName(dto.getSoldierName())
                .soldierRelation(dto.getSoldierRelation())
                .build();

        familyDetailsRepository.save(familyDetails);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("loggedUser", user);

        return "redirect:/family/dashboard";
    }


    @PostMapping("/register/volunteer")
    public String registerVolunteer(@Valid @ModelAttribute("registerVolunteerDto") RegisterVolunteerDto dto,
                                    BindingResult result,
                                    HttpSession session,
                                    Model model) {

        if (userService.emailExists(dto.getEmail())) {
            result.rejectValue("email", null, "אימייל זה כבר קיים במערכת");
        }
        if (userService.idNumberExists(dto.getIdNumber())) {
            result.rejectValue("idNumber", null, "תעודת זהות זו כבר רשומה במערכת");
        }


        if (result.hasErrors()) {
            return "volunteer/register_volunteer";
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword()) // בלי הצפנה!
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .approved(true)
                .idNumber(dto.getIdNumber())
                .role(Role.ROLE_VOLUNTEER)
                .build();

        // שמירת התמונה (אם יש)
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            imageUtils.saveProfileImage(dto.getImage(), user.getId());
            user.setProfileImagePath("/uploads/user_" + user.getId() + ".png");
        }

        // שמירת המשתמש (רק פעם אחת)
        userService.saveUser(user);

        // התחברות אוטומטית
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("loggedUser", user);



        return "redirect:/volunteer/dashboard";
    }

}