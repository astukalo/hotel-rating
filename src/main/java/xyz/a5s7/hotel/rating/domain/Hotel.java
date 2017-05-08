package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel {
    private String id;
    private String name;
    @JsonProperty("Reviews")
    private Collection<Review> reviews;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<Review> reviews) {
        this.reviews = reviews;
    }

    @JsonProperty("HotelInfo")
    private void unpackNameFromNestedObject(Map<String, String> hotelInfo) {
        id = hotelInfo.get("HotelID");
        name = hotelInfo.get("Name");
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
