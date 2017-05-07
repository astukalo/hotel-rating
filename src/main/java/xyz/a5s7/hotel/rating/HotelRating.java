package xyz.a5s7.hotel.rating;

import xyz.a5s7.hotel.rating.domain.*;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class HotelRating {
    public static void main(String[] args) throws IOException {
        File[] reviews = getReviews("/Users/a5s7/dev/projects/hotel-rating/src/main/resources");
        String topic = "";

        DomainParser domainParser = new DomainParser();
        TopicSearcher topicSearcher = new NaiveTopicSearcher();
        SynonymsMap synonymsMap = new SynonymsMap();
        Semantics semantics = new SemanticsParser().fromJson(
            new File("/Users/a5s7/dev/projects/hotel-rating/src/main/resources/semantics.json")
        );

        for (File review : reviews) {
            try {
                Hotel hotel = domainParser.fromJson(review);
                Collection<Review> reviewCollection = hotel.getReviews();
                List<String> synonyms = synonymsMap.findSynonyms(topic);
                Collection<Statistics> statistics = topicSearcher.findReviewsForTopic(synonyms, reviewCollection, semantics);
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
