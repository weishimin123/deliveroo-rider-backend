package com.deliveroo.rider.entity;

import com.deliveroo.rider.serialization.deserializer.AccountTypeDeserializer;
import com.deliveroo.rider.pojo.*;
import com.deliveroo.rider.serialization.serializer.AccountTypeSerializer;
import com.deliveroo.rider.serialization.serializer.CallingCodeSerializer;
import com.deliveroo.rider.serialization.serializer.CountrySerializer;
import com.deliveroo.rider.serialization.serializer.WorkingTypeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,length = 15,unique = true)
    private String firstName;

    @Column(nullable = false,length = 15,unique = true)
    private String lastname;

    @Column(nullable = false, length = 30,unique = true)
    private String phone;

    @Column( length = 5)
    @JsonSerialize(using = CallingCodeSerializer.class)
    private CallingCode callingCode = CallingCode.IRELAND;

    @Column(nullable = false, length = 30,unique = true)
    private String email;

    @Column(nullable = false,length = 10)
    @JsonDeserialize(using = AccountTypeDeserializer.class)
    @JsonSerialize(using = AccountTypeSerializer.class)
    private AccountType accountType;

    @Column(nullable = false)
    private String securityCode;

    @Column(length = 15)
    private City city;

    @Column(length = 10)
    @JsonSerialize(using = CountrySerializer.class)
    private Country country = Country.IRELAND;

    @Column(length = 10)
    @JsonSerialize(using = WorkingTypeSerializer.class)
    private WorkingType workingType = WorkingType.NORMAL;

    @Column(nullable = false, length = 10,unique = true)
    @Length(min = 6,max = 6)
    private String riderId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Activity> activities;

    @Column
    private boolean isLocked = false;

    @Column
    private boolean isEnabled = true;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;
}
