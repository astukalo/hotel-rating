package xyz.a5s7.hotel.rating;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.nlp.EnglishParser;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.util.Collection;
import java.util.List;

public class TopicSearcher {
    private EnglishParser englishParser = new EnglishParser();

    public Collection<Statistics> findReviewsForTopic(List<String> topic, Collection<Review> reviews) {
        for (Review review : reviews) {
            ImmutableList<List<HasWord>> sentences = englishParser.getSentences(review.getContent());
            for (List<HasWord> sentence : sentences) {
                for (HasWord word : sentence) {
                    
                }
            }
        }

        return null;
    }
}
