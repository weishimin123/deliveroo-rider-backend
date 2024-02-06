package com.deliveroo.rider.serialization.serializer;

import com.deliveroo.rider.pojo.Country;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CountrySerializer extends JsonSerializer<Country> {
    @Override
    public void serialize(Country country, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(country.getCountryName());
    }
}
