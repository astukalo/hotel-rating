package xyz.a5s7.hotel.rating.search;

import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.domain.Semantics;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.util.Collection;
import java.util.List;

public interface TopicSearcher {
    Collection<Statistics> findReviewsForTopic(List<String> topic, Collection<Review> reviews, Semantics semantics);
}
