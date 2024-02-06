package com.deliveroo.rider.converter;

import com.deliveroo.rider.pojo.WorkingType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkingTypeConverter implements Converter<String, WorkingType> {
    @Override
    public WorkingType convert(String source) {
        for (WorkingType workingType : WorkingType.values()) {
            if (workingType.name().equalsIgnoreCase(source)) {
                return workingType;
            }
        }
        throw new IllegalArgumentException("No enum constant with name: " + source);
    }
}