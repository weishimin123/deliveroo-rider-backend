package com.deliveroo.rider.controller;

import com.deliveroo.rider.entity.Account;
import com.deliveroo.rider.pojo.dto.CustomUserDetails;
import com.deliveroo.rider.pojo.dto.LoginRequest;
import com.deliveroo.rider.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed!");
        } else {
            Object principal = authenticate.getPrincipal();
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            Account account = customUserDetails.getAccount();
            String token = JwtUtil.generateToken(account);
            return ResponseEntity.ok(token);
        }
    }
}
