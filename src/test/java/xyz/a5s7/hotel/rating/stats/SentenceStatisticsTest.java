package xyz.a5s7.hotel.rating.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.PhraseMult;

import static org.junit.Assert.assertEquals;

public class SentenceStatisticsTest {
    @Test
    public void calcScore() throws Exception {
        SentenceStatistics sentenceStatistics = new SentenceStatistics();
        sentenceStatistics.setTopicCharacteristics(ImmutableMap.of(
                "good", new PhraseAdd("good", 1),
                "nice", new PhraseAdd("nice", 1.1f)
        ));
        sentenceStatistics.setCharacteristicsIntensifiers(ImmutableMap.of(
                "good", ImmutableList.of(new PhraseMult("perfectly", 1.5f)),
                "nice", ImmutableList.of(
                        new PhraseMult("very", 1.5f),
                        new PhraseMult("not", -1)
                )
        ));
        float v = sentenceStatistics.calcScore();
        assertEquals(1.5 + 1.1*1.5*(-1), v, 0.001);
    }


}