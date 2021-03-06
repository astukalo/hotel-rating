package xyz.a5s7.hotel.rating.nlp;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnglishParserTest {
    private EnglishParser englishParser = new EnglishParser();

    @Test
    public void shouldReturnSentences() throws Exception {
        List<List<? extends HasWord>> sentences = englishParser.getSentences("Hotel is clean and very close to airport. " +
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
        List<List<? extends HasWord>> sentences = englishParser.getSentences("Staff is more than helpful in most situations. ");
        assertTrue(englishParser.hasTopics(ImmutableList.of("staff", "personnel"), sentences.get(0)));
        assertFalse(englishParser.hasTopics(ImmutableList.of("restaurant", "cafe"), sentences.get(0)));
    }

    @Test
    public void shouldReturnAdjectivesForTopic() throws Exception {
        List<List<? extends HasWord>> sentences = englishParser.getSentences("This amazing hotel was rather clean and quite close, but not very nice");
        Map<String, List<TypedDependency>> valuedDependencies = englishParser.getValuedDependencies(sentences.get(0));

        List<IndexedWord> adjs = englishParser.getAdjectives(ImmutableList.of("hotel"), valuedDependencies);
        assertEquals(4, adjs.size());
        assertEquals("clean", adjs.get(0).value());
        assertEquals("amazing", adjs.get(1).value());
        assertEquals("close", adjs.get(2).value());
        assertEquals("nice", adjs.get(3).value());
    }

    @Test
    public void shouldFindIntensifiers() throws Exception {
        List<List<? extends HasWord>> sentences = englishParser.getSentences("This amazing hotel was rather clean and quite close, but not very nice");
        Map<String, List<TypedDependency>> valuedDependencies = englishParser.getValuedDependencies(sentences.get(0));

        final List<IndexedWord> intensifiers = englishParser.findIntensifiers("nice", valuedDependencies);
        for (IndexedWord word : intensifiers) {
            assertTrue(word.value().equals("not") || word.value().equals("very"));
        }
    }
}