package com.milufamilies.supportapp.model.enums;

public enum DaysAvailable {
    SUNDAY("ראשון"),
    MONDAY("שני"),
    TUESDAY("שלישי"),
    WEDNESDAY("רביעי"),
    THURSDAY("חמישי"),
    FRIDAY("שישי"),
    SATURDAY("שבת");

    private final String hebrew;

    DaysAvailable(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }
}
