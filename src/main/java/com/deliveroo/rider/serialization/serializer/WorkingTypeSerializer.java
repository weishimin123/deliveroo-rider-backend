package com.deliveroo.rider.serialization.serializer;

import com.deliveroo.rider.pojo.WorkingType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class WorkingTypeSerializer extends JsonSerializer<WorkingType> {
    @Override
    public void serialize(WorkingType workingType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(workingType.getValue());
    }
}
