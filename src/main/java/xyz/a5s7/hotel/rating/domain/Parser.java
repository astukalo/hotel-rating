package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Parser {
    public Hotel fromJson(File json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Hotel.class);
    }
}
