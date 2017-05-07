package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class DomainParser {
    private ObjectMapper mapper = new ObjectMapper();

    //TODO move to Hotel
    public Hotel fromJson(File json) throws IOException {
        return mapper.readValue(json, Hotel.class);
    }
}
