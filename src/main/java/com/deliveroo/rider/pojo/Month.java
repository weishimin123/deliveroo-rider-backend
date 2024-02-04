package com.deliveroo.rider.pojo;

public enum Month {
    JANUARY("Jan"),
    FEBRUARY("Feb"),
    MARCH("Mar"),
    APRIL("Apr"),
    MAY("May"),
    JUNE("Jun"),
    JULY("Jul"),
    August("Aug"),
    SEPTEMBER("Sep"),
    OCTOBER("Oct"),
    NOVEMBER("Nov"),
    DECEMBER("Dec");

    private String abbreviation;

    public String getAbbreviation() {
        return abbreviation;
    }

    Month(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
