package xyz.a5s7.hotel.rating.nlp;

import com.google.common.collect.ImmutableSet;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.*;

public class EnglishParser {

    private final MaxentTagger tagger;
    private final DependencyParser parser;
    private Morphology morpha = new Morphology();

    public EnglishParser() {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

        tagger = new MaxentTagger(taggerPath);
        parser = DependencyParser.loadFromModelFile(modelPath);
    }

    public List<List<? extends HasWord>> getSentences(String content) {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(content));
        List<List<? extends HasWord>> arrayList = new ArrayList<>();
        for (List<HasWord> hasWords : tokenizer) {
            List<? extends HasWord> tagged = tagger.tagCoreLabelsOrHasWords(hasWords, morpha, true);
            arrayList.add(tagged);

        }
        return arrayList;
    }

    public boolean hasTopics(List<String> topics, List<? extends HasWord> sentence) {
        for (HasWord word : sentence) {
            String w = ((CoreLabel)word).lemma();
            for (String topic : topics) {
                if (w.equals(topic)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get type of dependencies which may contain entity's characteristics
     * @param sentence tagged sentence
     * @return dependencies mapped by type
     */
    public Map<String, List<TypedDependency>> getValuedDependencies(List<? extends HasWord> sentence) {
        List<? extends HasWord> tagged = tagger.tagCoreLabelsOrHasWords(sentence, morpha, true);
        GrammaticalStructure gs = parser.predict(tagged);

        Map<String, List<TypedDependency>> valuedDependencies = new HashMap<>();
        Set<String> depTypes = ImmutableSet.of("nsubj", "amod", "conj", "advmod", "neg", "advcl");

        for (TypedDependency o : gs.allTypedDependencies()) {
            String shortName = o.reln().getShortName();
            if (depTypes.contains(shortName)) {
                List<TypedDependency> dependencyList = valuedDependencies.computeIfAbsent(shortName, s -> new ArrayList<>());
                dependencyList.add(o);
            }
        }
        return valuedDependencies;
    }

    public List<IndexedWord> getAdjectives(List<String> topics, Map<String, List<TypedDependency>> valuedDependencies) {
        List<IndexedWord> characteristics = new ArrayList<>();
        List<TypedDependency> nsubj = valuedDependencies.get("nsubj");
        if (nsubj != null) {
            for (TypedDependency typedDependency : nsubj) {
                for (String topic : topics) {
                    if (typedDependency.dep().lemma().equals(topic)) {
                        characteristics.add(typedDependency.gov());
                        break;
                    }
                }
            }
        }

        List<TypedDependency> amod = valuedDependencies.get("amod");
        if (amod != null) {
            for (TypedDependency typedDependency : amod) {
                for (String topic : topics) {
                    if (typedDependency.gov().lemma().equals(topic) && typedDependency.dep().tag().startsWith("JJ")) {
                        characteristics.add(typedDependency.dep());
                        break;
                    }
                }
            }
        }

        List<IndexedWord> conj = new ArrayList<>();
        List<TypedDependency> dependencies = valuedDependencies.get("conj");
        if (dependencies != null) {
            for (TypedDependency typedDependency : dependencies) {
                for (IndexedWord characteritic : characteristics) {
                    if (typedDependency.gov().equals(characteritic)) {
                        conj.add(typedDependency.dep());
                    }
                }
            }
        }
        characteristics.addAll(conj);

        List<IndexedWord> advcl = new ArrayList<>();
        dependencies = valuedDependencies.get("advcl");
        if (dependencies != null) {
            for (TypedDependency typedDependency : dependencies) {
                for (IndexedWord characteritic : characteristics) {
                    if (typedDependency.gov().equals(characteritic) && typedDependency.dep().tag().startsWith("JJ")) {
                        advcl.add(typedDependency.dep());
                    }
                }
            }
        }
        characteristics.addAll(advcl);

        return characteristics;
    }

    public List<IndexedWord> findIntensifiers(final String value, final Map<String, List<TypedDependency>> valuedDependencies) {
        List<IndexedWord> result = new ArrayList<>();
        List<TypedDependency> advmod = valuedDependencies.get("advmod");
        if (advmod != null) {
            for (TypedDependency dependency : advmod) {
                if (dependency.gov().lemma().equals(value)) {
                    result.add(dependency.dep());
                }
            }
        }
        List<TypedDependency> neg = valuedDependencies.get("neg");
        if (neg != null) {
            for (TypedDependency dependency : neg) {
                if (dependency.gov().lemma().equals(value)) {
                    result.add(dependency.dep());
                }
            }
        }
        return result;
    }
}
