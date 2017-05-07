package xyz.a5s7.hotel.rating.nlp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.Semantics;

import java.io.StringReader;
import java.util.*;
import java.util.function.BiFunction;

public class EnglishParser {

    private final MaxentTagger tagger;
    private final DependencyParser parser;

    public EnglishParser() {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

        tagger = new MaxentTagger(taggerPath);
        parser = DependencyParser.loadFromModelFile(modelPath);
    }

    public ImmutableList<List<HasWord>> getSentences(String content) {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(content));
        return ImmutableList.copyOf(tokenizer.iterator());
    }

    public boolean hasTopics(List<String> topics, List<HasWord> sentence) {
        for (HasWord word : sentence) {
            String w = word.word().toLowerCase();
            for (String topic : topics) {
                if (w.contains(topic)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, List<TypedDependency>> getValuedDependencies(List<HasWord> sentence) {
        List<TaggedWord> tagged = tagger.tagSentence(sentence);
        GrammaticalStructure gs = parser.predict(tagged);

        Map<String, List<TypedDependency>> valuedDependencies = new HashMap<>();
        Set<String> depTypes = ImmutableSet.of("nsubj", "amod", "conj", "advmod", "neg");

        for (TypedDependency o : gs.allTypedDependencies()) {
            String shortName = o.reln().getShortName();
            if (depTypes.contains(shortName)) {
                List<TypedDependency> dependencyList = valuedDependencies.computeIfAbsent(shortName, s -> new ArrayList<>());
                dependencyList.add(o);
            }
        }
        return valuedDependencies;
    }

    public List<IndexedWord> getAdjectives(List<String> topics, List<TypedDependency> depType, BiFunction<TypedDependency, String, Boolean> f) {
        List<IndexedWord> characteristics = new ArrayList<>();
        for (TypedDependency typedDependency : depType) {
            for (String topic : topics) {
                String t = topic.toLowerCase();
                if (f.apply(typedDependency, t)) {
                    characteristics.add(typedDependency.gov());
                    break;
                }
            }
        }

        return characteristics;
    }

    public Map<IndexedWord, PhraseAdd> filter(List<IndexedWord> adjs, Semantics semantics) {
        //could apply lemmatization before checking against semantics
        //TODO
        return null;
    }
}
