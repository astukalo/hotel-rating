package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.stanford.nlp.ling.IndexedWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Semantics {
    private List<PhraseAdd> positive;
    private Map<String, PhraseAdd> positiveMap;
    private List<PhraseAdd> negative;
    private Map<String, PhraseAdd> negativeMap;
    private List<PhraseMult> intensifier;
    private Map<String, PhraseMult> intensifierMap;
    private boolean cached = false;

    public List<PhraseAdd> getPositive() {
        return positive;
    }

    public void setPositive(List<PhraseAdd> positive) {
        this.positive = positive;
    }

    public List<PhraseAdd> getNegative() {
        return negative;
    }

    public void setNegative(List<PhraseAdd> negative) {
        this.negative = negative;
    }

    public List<PhraseMult> getIntensifier() {
        return intensifier;
    }

    public void setIntensifier(List<PhraseMult> intensifier) {
        this.intensifier = intensifier;
    }

    public void invalidate() {
        positiveMap = positive == null ? Collections.emptyMap() :
                positive.stream()
                .collect(Collectors.toMap(o -> o.getPhrase(), Function.identity(), (p1, p2) -> p1));
        negativeMap = negative == null ? Collections.emptyMap() :
                negative.stream()
                .collect(Collectors.toMap(o -> o.getPhrase(), Function.identity(), (p1, p2) -> p1));
        intensifierMap = intensifier == null ? Collections.emptyMap() :
                intensifier.stream()
                .collect(Collectors.toMap(o -> o.getPhrase(), Function.identity(), (p1, p2) -> p1));
        cached = true;
    }

    public Map<String, PhraseAdd> filterAdj(List<IndexedWord> adjs) {
        if (!cached) invalidate();

        Map<String, PhraseAdd> map = new HashMap<>();
        adjs.forEach(adj -> {
            PhraseAdd phraseAdd = positiveMap.get(adj.lemma());
            if (phraseAdd != null) {
                map.put(adj.value(), phraseAdd);
            }
        });
        adjs.forEach(adj -> {
            PhraseAdd phraseAdd = negativeMap.get(adj.lemma());
            if (phraseAdd != null) {
                map.put(adj.value(), phraseAdd);
            }
        });

        return map;
    }

    public List<PhraseMult> filterIntensifiers(final List<IndexedWord> intensifiers) {
        if (!cached) invalidate();

        List<PhraseMult> list = new ArrayList<>();
        intensifiers.forEach(w -> {
            PhraseMult phraseMult = intensifierMap.get(w.lemma());
            if (phraseMult != null) {
                list.add(phraseMult);
            }
        });

        return list;
    }
}
