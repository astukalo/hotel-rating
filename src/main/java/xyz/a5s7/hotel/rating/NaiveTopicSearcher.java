package xyz.a5s7.hotel.rating;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;
import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.domain.Semantics;
import xyz.a5s7.hotel.rating.nlp.EnglishParser;
import xyz.a5s7.hotel.rating.nlp.Utils;
import xyz.a5s7.hotel.rating.stats.SentenceStatistics;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.util.*;

public class NaiveTopicSearcher implements TopicSearcher {
    private EnglishParser englishParser = new EnglishParser();

    public Collection<Statistics> findReviewsForTopic(List<String> topics, Collection<Review> reviews, Semantics semantics) {
        List<PhraseAdd> negative = semantics.getNegative();
        Map<String, PhraseAdd> negOneWordPhrases = new HashMap<>();
        Map<String, PhraseAdd> negPhrases = new HashMap<>();

        for (PhraseAdd phraseAdd : negative) {
            if (phraseAdd.getPhrase().split(" ").length > 1) {
                negOneWordPhrases.put(phraseAdd.getPhrase(), phraseAdd);
            } else {
                negPhrases.put(phraseAdd.getPhrase(), phraseAdd);
            }
        }

        Map<String, PhraseAdd> posOneWordPhrases = new HashMap<>();
        Map<String, PhraseAdd> posPhrases = new HashMap<>();

        for (PhraseAdd phraseAdd : semantics.getPositive()) {
            if (phraseAdd.getPhrase().split(" ").length > 1) {
                posOneWordPhrases.put(phraseAdd.getPhrase(), phraseAdd);
            } else {
                posPhrases.put(phraseAdd.getPhrase(), phraseAdd);
            }
        }

        ArrayList<Statistics> statisticsArrayList = new ArrayList<>();

        for (Review review : reviews) {
            ImmutableList<List<HasWord>> sentences = englishParser.getSentences(review.getContent());
            for (List<HasWord> sentence : sentences) {
                if (englishParser.hasTopics(topics, sentence)) {
                    Map<String, List<TypedDependency>> valuedDependencies = englishParser.getValuedDependencies(sentence);

                    List<IndexedWord> adjs = englishParser.getAdjectives(topics,
                            valuedDependencies.get("nsubj"),
                            (typedDependency, topic) ->
                                    typedDependency.dep().value().contains(topic) && typedDependency.gov().tag().startsWith("JJ"));
                    //could apply lemmatization before checking against semantics
                    adjs.addAll(
                            englishParser.getAdjectives(topics,
                                    valuedDependencies.get("amod"),
                                    (typedDependency, topic) ->
                                            typedDependency.gov().value().contains(topic) && typedDependency.dep().tag().startsWith("JJ"))
                    );

                    Map<IndexedWord, PhraseAdd> filtered = englishParser.filter(adjs, semantics);


                    Statistics statistics = new Statistics();
                    statistics.setReview(review);
                    statistics.setSentenceStatistics(new ArrayList<>());

                    statisticsArrayList.add(statistics);
                    calcScore(posOneWordPhrases, sentence, statistics);
                }
            }
        }

        return null;
    }

    private void calcScore(Map<String, PhraseAdd> posOneWordPhrases, List<HasWord> sentence, Statistics statistics) {
        SentenceStatistics st = new SentenceStatistics();
        st.setContent(Utils.asString(sentence));
        st.setAdjWithIntensifier(new HashMap<>());
        for (HasWord word : sentence) {
            PhraseAdd ph = posOneWordPhrases.get(word.word().toLowerCase());
            if (ph != null) {
                st.setScore(ph.getValue());
                st.getAdjWithIntensifier().putIfAbsent(ph.getPhrase(), new ArrayList<>());
                statistics.getSentenceStatistics().add(
                    st
                );
            }
        }
    }


}
