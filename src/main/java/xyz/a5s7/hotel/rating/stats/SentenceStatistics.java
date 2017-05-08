package xyz.a5s7.hotel.rating.stats;

import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.PhraseMult;

import java.util.List;
import java.util.Map;

public class SentenceStatistics {
    private String content;
    Map<String, PhraseAdd> filteredAdj;
    Map<String, List<PhraseMult>> filteredIntensifiers;
    private float score;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getScore() {
        return score;
    }

    public Map<String, PhraseAdd> getFilteredAdj() {
        return filteredAdj;
    }

    public void setTopicCharacteristics(final Map<String, PhraseAdd> filteredAdj) {
        this.filteredAdj = filteredAdj;
    }

    public Map<String, List<PhraseMult>> getFilteredIntensifiers() {
        return filteredIntensifiers;
    }

    public void setCharacteristicsIntensifiers(final Map<String, List<PhraseMult>> filteredIntensifiers) {
        this.filteredIntensifiers = filteredIntensifiers;
    }

    public void setScore(final float score) {
        this.score = score;
    }

    public float calcScore() {
        float total = 0;
        if (filteredAdj != null) {
            for (Map.Entry<String, PhraseAdd> entry : filteredAdj.entrySet()) {
                String word = entry.getKey();
                float score = entry.getValue().getValue();
                if (score > 0 && filteredIntensifiers != null && !filteredIntensifiers.isEmpty()) {
                    List<PhraseMult> multList = filteredIntensifiers.get(word);
                    if (multList != null) {
                        for (PhraseMult phraseMult : multList) {
                            score *= phraseMult.getMultiplier();
                        }
                    }
                }
                total += score;
            }
        }
        score = total;
        return total;
    }

    @Override
    public String toString() {
        return "SentenceStatistics{" +
                "content='" + content + '\'' +
                ", filteredAdj=" + filteredAdj +
                ", filteredIntensifiers=" + filteredIntensifiers +
                ", score=" + score +
                '}';
    }
}
