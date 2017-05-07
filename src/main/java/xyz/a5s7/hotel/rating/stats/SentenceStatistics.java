package xyz.a5s7.hotel.rating.stats;

import java.util.List;
import java.util.Map;

public class SentenceStatistics {
    private String content;
    private Map<String, List<String>> adjWithIntensifier;
    private int score;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, List<String>> getAdjWithIntensifier() {
        return adjWithIntensifier;
    }

    public void setAdjWithIntensifier(Map<String, List<String>> adjWithIntensifier) {
        this.adjWithIntensifier = adjWithIntensifier;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SentenceStatistics that = (SentenceStatistics) o;

        if (!content.equals(that.content)) return false;
        return adjWithIntensifier.equals(that.adjWithIntensifier);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + adjWithIntensifier.hashCode();
        return result;
    }
}
