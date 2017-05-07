package xyz.a5s7.hotel.rating;

import xyz.a5s7.hotel.rating.domain.Hotel;
import xyz.a5s7.hotel.rating.domain.Parser;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class HotelRating {
    public static void main(String[] args) {
        File[] reviews = getReviews("/Users/a5s7/dev/projects/hotel-rating/src/main/resources");
        String topic = "";

        Parser parser = new Parser();
        TopicSearcher topicSearcher = new TopicSearcher();

        for (File review : reviews) {
            try {
                Hotel hotel = parser.fromJson(review);
                Collection<Review> reviewCollection = hotel.getReviews();
                Collection<Statistics> statistics = topicSearcher.findReviews(topic, reviewCollection);
            } catch (IOException e) {
                System.out.println("Unable to parse file " + review.getAbsolutePath());
            }
        }
    }

    private static File[] getReviews(String path) {
        File dir = new File(path);
        return dir.listFiles((dir1, name) -> name.contains("review"));
    }
}
