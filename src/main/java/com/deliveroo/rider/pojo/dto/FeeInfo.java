package com.deliveroo.rider.pojo.dto;

import com.deliveroo.rider.entity.FeeBoost;
import lombok.Data;

import java.util.List;

@Data
public class FeeInfo {
    private String date;
    private List<FeeBoost> feeList;
}
