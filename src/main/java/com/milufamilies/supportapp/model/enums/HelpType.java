package com.milufamilies.supportapp.model.enums;

import lombok.Getter;

@Getter
public enum HelpType {
    COOKING("Cooking", "בישול"),
    BABYSITTING("Babysitting", "שמרטפות"),
    CLEANING("Cleaning", "ניקיון"),
    GROCERY_SHOPPING("Grocery Shopping", "קניות"),
    MEDICAL_APPOINTMENTS_ASSISTANCE("Medical Appointments Assistance", "ליווי לרופא"),
    TRANSPORTATION("Transportation", "הסעות"),
    PET_SITTING("Pet Sitting", "טיפול בחיות"),
    HOMEWORK_HELP("Homework Help", "עזרה בשיעורי בית"),
    TECHNICAL_SUPPORT("Technical Support", "תמיכה טכנית"),
    OTHER("Other", "אחר");

    private final String displayName;
    private final String hebrew;

    HelpType(String displayName, String hebrew) {
        this.displayName = displayName;
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }
}
