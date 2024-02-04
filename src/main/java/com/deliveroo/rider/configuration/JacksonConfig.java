package com.deliveroo.rider.configuration;

import com.deliveroo.rider.entity.FeeBoost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new SimpleModule().addSerializer(FeeBoost.class,new FeeBoostSerializer()));
        return objectMapper;
    }
}
