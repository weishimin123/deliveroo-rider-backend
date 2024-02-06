package com.deliveroo.rider.pojo.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
