package xyz.a5s7.hotel.rating;

import xyz.a5s7.hotel.rating.domain.Hotel;
import xyz.a5s7.hotel.rating.domain.DomainParser;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class HotelRating {
    public static void main(String[] args) {
        File[] reviews = getReviews("/Users/a5s7/dev/projects/hotel-rating/src/main/resources");
        String topic = "";

        DomainParser domainParser = new DomainParser();
        TopicSearcher topicSearcher = new TopicSearcher();
        SynonymsMap synonymsMap = new SynonymsMap();

        for (File review : reviews) {
            try {
                Hotel hotel = domainParser.fromJson(review);
                Collection<Review> reviewCollection = hotel.getReviews();
                List<String> synonyms = synonymsMap.findSynonyms(topic);
                Collection<Statistics> statistics = topicSearcher.findReviewsForTopic(synonyms, reviewCollection);
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
