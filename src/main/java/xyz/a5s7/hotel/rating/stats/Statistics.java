package xyz.a5s7.hotel.rating.stats;

import xyz.a5s7.hotel.rating.domain.Review;

import java.util.List;

public class Statistics {
    private Review review;
    private List<SentenceStatistics> sentenceStatistics;

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<SentenceStatistics> getSentenceStatistics() {
        return sentenceStatistics;
    }

    public void setSentenceStatistics(List<SentenceStatistics> sentenceStatistics) {
        this.sentenceStatistics = sentenceStatistics;
    }
}
