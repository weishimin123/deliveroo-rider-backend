package com.deliveroo.rider.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummary {
    private String shop;
    @JsonFormat(pattern = "HH:mm",locale = "en")
    private LocalTime complete;
    private double earnings;
    private int subOrders;
    private Long id;
}
