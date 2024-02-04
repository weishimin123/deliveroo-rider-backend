package com.deliveroo.rider.pojo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyActivity extends WeeklyActivitySummary{
    private int activityDays;
    @JsonProperty("dayActivities")
    private List<DailyActivitySummary> dailyActivities;
}
