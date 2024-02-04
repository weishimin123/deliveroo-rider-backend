package com.deliveroo.rider.controller;

import com.deliveroo.rider.entity.Account;
import com.deliveroo.rider.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository repository;

    @PutMapping("/account")
    public void createAccount(@RequestBody Account account){

    }
}
