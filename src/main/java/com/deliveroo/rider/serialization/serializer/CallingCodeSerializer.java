package com.deliveroo.rider.serialization.serializer;

import com.deliveroo.rider.pojo.CallingCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CallingCodeSerializer extends JsonSerializer<CallingCode> {
    @Override
    public void serialize(CallingCode callingCode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(callingCode.getCode());
    }
}
