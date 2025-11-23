package com.milufamilies.supportapp.model.enums;

public enum TimeSlot {
    MORNING("בוקר"),
    AFTERNOON("צהריים"),
    EVENING("ערב"),
    NIGHT("לילה");

    private final String hebrew;

    TimeSlot(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }
}
