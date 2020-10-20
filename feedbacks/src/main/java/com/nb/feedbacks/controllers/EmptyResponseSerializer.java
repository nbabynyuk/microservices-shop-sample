package com.nb.feedbacks.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class EmptyResponseSerializer extends StdSerializer<EmptyResponse> {

    protected EmptyResponseSerializer() {
        super(EmptyResponse.class);
    }

    @Override
    public void serialize(EmptyResponse emptyResponse,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
    }
}
