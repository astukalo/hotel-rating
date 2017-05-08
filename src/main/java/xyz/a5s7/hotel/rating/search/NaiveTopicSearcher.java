package xyz.a5s7.hotel.rating.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.TypedDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.a5s7.hotel.rating.domain.PhraseAdd;
import xyz.a5s7.hotel.rating.domain.PhraseMult;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.domain.Semantics;
import xyz.a5s7.hotel.rating.nlp.EnglishParser;
import xyz.a5s7.hotel.rating.nlp.Utils;
import xyz.a5s7.hotel.rating.stats.SentenceStatistics;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveTopicSearcher implements TopicSearcher {
    private final Logger log = LoggerFactory.getLogger(NaiveTopicSearcher.class);
    private EnglishParser englishParser = new EnglishParser();

    public Collection<Statistics> findReviewsForTopic(List<String> topics, Collection<Review> reviews, Semantics semantics) {
        ArrayList<Statistics> statisticsArrayList = new ArrayList<>();

        for (Review review : reviews) {
            List<List<? extends HasWord>> sentences = englishParser.getSentences(review.getContent());
            Statistics statistics = new Statistics(review);
            for (List<? extends HasWord> sentence : sentences) {
                if (englishParser.hasTopics(topics, sentence)) {
                    //TODO better to analyze SemanticGraph
                    Map<String, List<TypedDependency>> valuedDependencies = englishParser.getValuedDependencies(sentence);

                    Map<String, PhraseAdd> filteredAdj = findTopicCharacteristics(topics, semantics, valuedDependencies);
                    Map<String, List<PhraseMult>> filteredIntensifiers = findCharacteristicsIntensifiers(semantics, valuedDependencies, filteredAdj);

                    SentenceStatistics sentenceStatistics = createSentenceStatistics(sentence, filteredAdj, filteredIntensifiers);
                    statistics.addSentenceStatistics(sentenceStatistics);
                }
            }
            if (!statistics.getSentenceStatistics().isEmpty()) {
                statistics.calcScore();
                statisticsArrayList.add(statistics);
                log.debug(statistics.toString());
            }
        }

        return statisticsArrayList;
    }

    private SentenceStatistics createSentenceStatistics(List<? extends HasWord> sentence,
                                                        Map<String, PhraseAdd> filteredAdj,
                                                        Map<String, List<PhraseMult>> filteredIntensifiers) {
        SentenceStatistics sentenceStatistics = new SentenceStatistics();
        sentenceStatistics.setContent(Utils.asString(sentence));
        sentenceStatistics.setTopicCharacteristics(filteredAdj);
        sentenceStatistics.setCharacteristicsIntensifiers(filteredIntensifiers);
        return sentenceStatistics;
    }

    private Map<String, List<PhraseMult>> findCharacteristicsIntensifiers(Semantics semantics,
                                                                          Map<String, List<TypedDependency>> valuedDependencies,
                                                                          Map<String, PhraseAdd> filteredAdj) {
        Map<String, List<PhraseMult>> filteredIntensifiers = new HashMap<>();
        for (String s : filteredAdj.keySet()) {
            List<IndexedWord> intensifiers = englishParser.findIntensifiers(s, valuedDependencies);
            List<PhraseMult> filteredInts = semantics.filterIntensifiers(intensifiers);
            if (filteredInts != null) {
                filteredIntensifiers.put(s, filteredInts);
            }
        }
        return filteredIntensifiers;
    }

    private Map<String, PhraseAdd> findTopicCharacteristics(List<String> topics,
                                                            Semantics semantics, Map<String,
                                                            List<TypedDependency>> valuedDependencies) {
        List<IndexedWord> adjs = englishParser.getAdjectives(topics, valuedDependencies);
        return semantics.filterAdj(adjs);
    }
}
