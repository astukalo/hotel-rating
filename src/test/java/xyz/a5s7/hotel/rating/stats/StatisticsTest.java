package xyz.a5s7.hotel.rating.stats;

import org.junit.Test;
import xyz.a5s7.hotel.rating.domain.Review;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsTest {
    @Test
    public void calcScore() throws Exception {
        Statistics statistics = new Statistics(new Review());
        SentenceStatistics sentenceStatistics = mock(SentenceStatistics.class);
        when(sentenceStatistics.calcScore()).thenReturn(12.3f);
        statistics.addSentenceStatistics(sentenceStatistics);
        SentenceStatistics sentenceStatistics1 = mock(SentenceStatistics.class);
        when(sentenceStatistics1.calcScore()).thenReturn(3.3f);
        statistics.addSentenceStatistics(sentenceStatistics1);
        SentenceStatistics sentenceStatistics2 = mock(SentenceStatistics.class);
        when(sentenceStatistics2.calcScore()).thenReturn(4.4f);
        statistics.addSentenceStatistics(sentenceStatistics2);

        statistics.calcScore();
        assertEquals(20, statistics.getScore(), 0.001);
    }

}