package com.deliveroo.rider.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonthlyActivity extends MonthlyActivitySummary{

    private int activityDays;

    @JsonProperty("dayActivities")
    private List<DailyActivitySummary> dailyActivities;

    public MonthlyActivity(String monthAbbreviation,
                           int orders,
                           double monthlyEarnings,
                           int activityDays,
                           List<DailyActivitySummary> dailyActivities){
        super(monthAbbreviation, orders, monthlyEarnings);
        this.activityDays = activityDays;
        this.dailyActivities = dailyActivities;
    }
}
