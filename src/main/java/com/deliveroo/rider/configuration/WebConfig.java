package com.deliveroo.rider.configuration;

import com.deliveroo.rider.component.converter.DayOfWeekConverter;
import com.deliveroo.rider.component.converter.MonthConverter;
import com.deliveroo.rider.component.converter.WorkingTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MonthConverter());
        registry.addConverter(new WorkingTypeConverter());
        registry.addConverter(new DayOfWeekConverter());
    }
}
