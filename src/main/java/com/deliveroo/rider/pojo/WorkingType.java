package com.deliveroo.rider.pojo;

public enum WorkingType {
    BUSY("busy"),
    NORMAL("normal"),
    EASY("easy");

    private String value;
    WorkingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
