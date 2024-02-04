package com.deliveroo.rider.pojo;

public enum CallingCode {
    IRELAND("+353"),
    UK("+44");

    private String code;
    CallingCode(String code) {
        this.code = code;
    }
}
