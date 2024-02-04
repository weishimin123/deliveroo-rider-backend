package com.deliveroo.rider.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "custom")
@Data
public class CustomProperties {
    private List<String> places = new ArrayList<>();
    private  List<String> shops = new ArrayList<>();
    private Map<String,List<Map<String,Object>>> feeBoost = new HashMap<>();
}
