package com.deliveroo.rider.converter;

import com.deliveroo.rider.pojo.DayOfWeek;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DayOfWeekConverter implements Converter<String, DayOfWeek> {
    @Override
    public DayOfWeek convert(String source) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.name().equalsIgnoreCase(source)) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("No enum constant with name: " + source);
    }
}