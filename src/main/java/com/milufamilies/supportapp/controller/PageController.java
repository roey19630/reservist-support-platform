package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.dto.RegisterFamilyDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.milufamilies.supportapp.dto.RegisterVolunteerDto;
import com.milufamilies.supportapp.model.enums.SoldierRelation;

@Controller
public class PageController {

    // עמוד הבית
    @GetMapping("/")
    public String home() {
        return "index";  // מציג את index.html
    }

    // עמוד התחברות
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // מציג את login.html
    }

    // עמוד בחירת סוג הרשמה
    @GetMapping("/register")
    public String showRegisterOptions() {
        return "register";  // מציג את register.html (שמציג כפתורים למשפחה/מתנדב)
    }

    // עמוד הרשמת משפחה עם טופס
    @GetMapping("/register/family")
    public String showRegisterFamilyForm(Model model) {
        model.addAttribute("registerFamilyDto", new RegisterFamilyDto());
        model.addAttribute("relations", SoldierRelation.values()); // 🆕
        return "families/register_family";
    }


    @GetMapping("/register/volunteer")
    public String showRegisterVolunteerForm(Model model) {
        model.addAttribute("registerVolunteerDto", new RegisterVolunteerDto());
        return "volunteer/register_volunteer";  // מציג את register_volunteer.html
    }
}
