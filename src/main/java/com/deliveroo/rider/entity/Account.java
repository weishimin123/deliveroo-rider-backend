package com.deliveroo.rider.entity;

import com.deliveroo.rider.pojo.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 5)
    private CallingCode callingCode;

    @Column(nullable = false, length = 30)
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

    @Column(nullable = false, length = 10)
    private String riderId;
}
