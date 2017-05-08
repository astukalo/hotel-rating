package xyz.a5s7.hotel.rating.domain;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SemanticsTest {
    @Test
    public void shouldFilterAdjs() throws Exception {
        final List<IndexedWord> adjs = ImmutableList.of(
                new IndexedWord(CoreLabel.wordFromString("good")),
                new IndexedWord(CoreLabel.wordFromString("bad")),
                new IndexedWord(CoreLabel.wordFromString("ugly")),
                new IndexedWord(CoreLabel.wordFromString("excellent"))
        );
        adjs.forEach(indexedWord -> indexedWord.setLemma(indexedWord.word()));

        Semantics semantics = new Semantics();
        semantics.setPositive(
                ImmutableList.of(
                        new PhraseAdd("good", 1),
                        new PhraseAdd("awesome", 1)
                )
        );
        semantics.setNegative(
                ImmutableList.of(
                        new PhraseAdd("ugly", 1),
                        new PhraseAdd("unfriendly", 1)
                )
        );
        Map<String, PhraseAdd> map = semantics.filterAdj(adjs);
        assertEquals(new PhraseAdd("good", 1), map.get("good"));
        assertEquals(new PhraseAdd("ugly", 1), map.get("ugly"));
    }

    @Test
    public void shouldFilterIntensifiers() throws Exception {
        Semantics semantics = new Semantics();
        semantics.setIntensifier(
                ImmutableList.of(
                        new PhraseMult("great", 1.2f),
                        new PhraseMult("not", -1)
                )
        );
        final List<IndexedWord> intens = ImmutableList.of(
                new IndexedWord(CoreLabel.wordFromString("great")),
                new IndexedWord(CoreLabel.wordFromString("not")),
                new IndexedWord(CoreLabel.wordFromString("unlikely"))
        );
        intens.forEach(indexedWord -> indexedWord.setLemma(indexedWord.word()));

        List<PhraseMult> phraseMults = semantics.filterIntensifiers(intens);
        assertEquals(new PhraseMult("great", 1.2f),phraseMults.get(0));
        assertEquals(new PhraseMult("not", -1),phraseMults.get(1));
    }
}