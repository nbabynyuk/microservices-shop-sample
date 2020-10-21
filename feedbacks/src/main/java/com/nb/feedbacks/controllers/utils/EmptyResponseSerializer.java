package com.nb.feedbacks.controllers.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class EmptyResponseSerializer extends StdSerializer<EmptyResponse> {

    protected EmptyResponseSerializer() {
        super(EmptyResponse.class);
    }

    @Override
    public void serialize(EmptyResponse emptyResponse,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) {
    }
}
