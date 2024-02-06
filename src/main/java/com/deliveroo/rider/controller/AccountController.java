package com.deliveroo.rider.controller;

import com.deliveroo.rider.entity.Account;
import com.deliveroo.rider.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping("/account")
    public ResponseEntity<Account> createAccount(@RequestBody @Validated Account account){
        Optional<Account> phoneOptional = repository.findByIdentity(account.getPhone());
        Optional<Account> emailOptional = repository.findByIdentity(account.getEmail());
        if(emailOptional.isPresent() || phoneOptional.isPresent()){
            return ResponseEntity.badRequest().build();
        }else {
            account.setSecurityCode(
                    passwordEncoder.encode(account.getSecurityCode()));
            Account saved = repository.save(account);
            return ResponseEntity.ok(saved);
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> searchAccountById(@PathVariable("id") Long id){
        Optional<Account> optional = repository.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
