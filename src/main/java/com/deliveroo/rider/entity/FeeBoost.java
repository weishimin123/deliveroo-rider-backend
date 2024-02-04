package com.deliveroo.rider.entity;

import com.deliveroo.rider.pojo.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
@Data
public class FeeBoost {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,length = 10)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime start;

    @Column(nullable = false)
    private LocalTime complete;

    @Column(nullable = false,precision = 1)
    private Double rate;
}
