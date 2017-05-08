package xyz.a5s7.hotel.rating.stats;

import xyz.a5s7.hotel.rating.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private Review review;
    private List<SentenceStatistics> sentenceStatistics;
    private float score;

    public Statistics(final Review review) {
        this.review = review;
        sentenceStatistics = new ArrayList<>();
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<SentenceStatistics> getSentenceStatistics() {
        return sentenceStatistics;
    }

    public List<SentenceStatistics> addSentenceStatistics(SentenceStatistics statistics) {
        sentenceStatistics.add(statistics);
        return sentenceStatistics;
    }

    public void setSentenceStatistics(List<SentenceStatistics> sentenceStatistics) {
        this.sentenceStatistics = sentenceStatistics;
    }

    public float calcScore() {
        if (sentenceStatistics != null) {
            score = (float) sentenceStatistics.stream().mapToDouble(statistics -> statistics.calcScore()).sum();
        }
        return score;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "review=" + review +
                ", sentenceStatistics=" + sentenceStatistics +
                ", score=" + score +
                '}';
    }
}
