package com.deliveroo.rider.entity;

import com.deliveroo.rider.pojo.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30,unique = true)
    private String phone;

    @Column(nullable = false, length = 5)
    private CallingCode callingCode;

    @Column(nullable = false, length = 30,unique = true)
    private String email;

    @Column(nullable = false,length = 10)
    private AccountType accountType;

    @Column(nullable = false,length = 10)
    private String securityCode;

    @Column(nullable = false,length = 15)
    private City city;

    @Column(nullable = false,length = 10)
    private Country country;

    @Column(length = 10)
    private WorkingType workingType;

    @Column(nullable = false, length = 10,unique = true)
    private String riderId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Activity> activities;
}
