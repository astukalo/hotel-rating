package rating.search;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.domain.Semantics;
import xyz.a5s7.hotel.rating.search.NaiveTopicSearcher;
import xyz.a5s7.hotel.rating.search.TopicSearcher;
import xyz.a5s7.hotel.rating.stats.SentenceStatistics;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.util.*;

import static org.junit.Assert.*;

public class TopicSearcherTest {


    private TopicSearcher topicSearcher = new NaiveTopicSearcher();

    @Test
    public void findReviewsForTopic() throws Exception {
        Review r = new Review();
        r.setId("123");
        r.setContent("Hotel is clean and very close to airport. " +
                "Staff is more than helpful in most situations. " +
                "Restaurant is fine, but I would suggest eating out for " +
                "breakfast as their breakfast menu is extremely high priced. " +
                "$28 for two and one of us only had toast and juice. " +
                "Their lunch and dinners are more in line with what cost should be. " +
                "Overall a good experience and I have been staying there for years. " +
                "As holiday Inn advises..... No surprises.");
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(r);
        Semantics semantics = new Semantics();
        PhraseAdd phraseAdd = new PhraseAdd("helpful", 1.0f);
        semantics.setPositive(ImmutableList.of(phraseAdd));
        Collection<Statistics> stats = topicSearcher.findReviewsForTopic(ImmutableList.of("staff"), reviews, semantics);
        Statistics statistics = stats.iterator().next();
        assertEquals(r, statistics.getReview());

        SentenceStatistics expected = new SentenceStatistics();
        expected.setContent("Staff is more than helpful in most situations.");

        HashMap<String, PhraseAdd> adjWithIntensifier = new HashMap<>();
        adjWithIntensifier.put("helpful", phraseAdd);
        expected.setTopicCharacteristics(adjWithIntensifier);
        expected.calcScore();
        assertEquals(expected.getScore(), statistics.getSentenceStatistics().get(0).getScore(), 0.001);
        assertEquals(expected.getFilteredAdj(), statistics.getSentenceStatistics().get(0).getFilteredAdj());
    }

}