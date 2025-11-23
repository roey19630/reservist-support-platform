package com.milufamilies.supportapp.model.enums;

public enum SoldierRelation {
    SON("בן"),
    DAUGHTER("בת"),
    FATHER("אבא"),
    MOTHER("אמא"),
    BROTHER("אח"),
    SISTER("אחות");

    private final String hebrew;

    SoldierRelation(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }
}
