package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SemanticsParser {
    private ObjectMapper mapper = new ObjectMapper();

    //TODO move to Semantics
    public Semantics fromJson(File json) throws IOException {
        return mapper.readValue(json, Semantics.class);
    }
}
