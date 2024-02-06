package com.deliveroo.rider.service;

import com.deliveroo.rider.entity.Account;
import com.deliveroo.rider.pojo.dto.CustomUserDetails;
import com.deliveroo.rider.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository repository;

    /**
     *
     * @param identity can be phone number or email
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {
        Optional<Account> optional = repository.findByIdentity(identity);
        return optional.map(CustomUserDetails::new).orElse(null);
    }
}
