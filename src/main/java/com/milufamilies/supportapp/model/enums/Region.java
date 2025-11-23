package com.milufamilies.supportapp.model.enums;

public enum Region {
    NORTH("צפון"),
    HAIFA("חיפה"),
    CENTER("מרכז"),
    TEL_AVIV("תל אביב"),
    JERUSALEM("ירושלים"),
    SOUTH("דרום"),
    EILAT("אילת");

    private final String hebrew;

    Region(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }
}
