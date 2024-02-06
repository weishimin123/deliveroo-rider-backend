package com.deliveroo.rider.pojo;

public enum Country {
    IRELAND("Ireland"),
    UK("United Kingdom");

    private String countryName;

    Country(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return this.countryName;
    }
}
