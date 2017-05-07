package rating.domain;

import org.junit.Test;
import xyz.a5s7.hotel.rating.domain.Hotel;
import xyz.a5s7.hotel.rating.domain.Parser;
import xyz.a5s7.hotel.rating.domain.Review;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    Parser parser = new Parser();

    @Test
    public void fromJson() throws Exception {
        File reviewFile = new File(this.getClass().getResource("/review_test.json").getFile());
        Hotel hotel = parser.fromJson(reviewFile);
        Collection<Review> reviews = hotel.getReviews();
        assertEquals(2, reviews.size());
        assertEquals("77923", hotel.getId());
        assertEquals("Holiday Inn Los Angeles International Airport", hotel.getName());
        Iterator<Review> iterator = reviews.iterator();
        assertEquals("UR127417978", iterator.next().getId());
        assertEquals("UR127319807", iterator.next().getId());
    }

}