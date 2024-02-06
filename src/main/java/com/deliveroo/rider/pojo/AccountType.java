package com.deliveroo.rider.pojo;

public enum AccountType {
    BIKE("bike"),
    E_BIKE("e-bike"),
    CAR("car");

    private String value;
    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
