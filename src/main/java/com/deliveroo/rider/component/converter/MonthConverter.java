package com.deliveroo.rider.component.converter;

import com.deliveroo.rider.pojo.Month;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MonthConverter implements Converter<String, Month> {

    @Override
    public Month convert(String source) {
        for (Month month : Month.values()) {
            if (month.name().equalsIgnoreCase(source)) {
                return month;
            }
        }
        throw new IllegalArgumentException("No enum constant with name: " + source);
    }
}