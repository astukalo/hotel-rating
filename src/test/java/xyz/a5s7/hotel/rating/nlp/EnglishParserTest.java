package xyz.a5s7.hotel.rating.nlp;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EnglishParserTest {

    private EnglishParser englishParser = new EnglishParser();

    @Test
    public void shouldReturnSentences() throws Exception {
        ImmutableList<List<HasWord>> sentences = englishParser.getSentences("Hotel is clean and very close to airport. " +
                "Staff is more than helpful in most situations. " +
                "Restaurant is fine, but I would suggest eating out for " +
                "breakfast as their breakfast menu is extremely high priced. " +
                "$28 for two and one of us only had toast and juice. " +
                "Their lunch and dinners are more in line with what cost should be. " +
                "Overall a good experience and I have been staying there for years. " +
                "As holiday Inn advises..... No surprises.");
        assertEquals(8, sentences.size());
        assertEquals("Staff is more than helpful in most situations .", Utils.asString(sentences.get(1)));
    }

    @Test
    public void shouldFindSentenceWithTopic() throws Exception {
        ImmutableList<List<HasWord>> sentences = englishParser.getSentences("Staff is more than helpful in most situations. ");
        assertTrue(englishParser.hasTopics(ImmutableList.of("staff", "personnel"), sentences.get(0)));
        assertFalse(englishParser.hasTopics(ImmutableList.of("restaurant", "cafe"), sentences.get(0)));
    }
}