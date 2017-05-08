package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class SemanticsParser {
    private ObjectMapper mapper = new ObjectMapper();

    //TODO move to Semantics
    public Semantics fromJson(InputStream json) throws IOException {
        return mapper.readValue(json, Semantics.class);
    }
}
