package com.deliveroo.rider.configuration;

import com.deliveroo.rider.entity.FeeBoost;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FeeBoostSerializer extends JsonSerializer<FeeBoost> {
    @Override
    public void serialize(FeeBoost feeBoost,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        jsonGenerator.writeStringField("timeRange", String.format("%s-%s",
                feeBoost.getStart().format(formatter),
                feeBoost.getComplete().format(formatter)));
//        jsonGenerator.writeStringField("day",feeBoost.getDayOfWeek().toString());
        jsonGenerator.writeStringField("detail",feeBoost.getRate()+"x extra per order");
        jsonGenerator.writeEndObject();
    }
}
