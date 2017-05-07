package xyz.a5s7.hotel.rating.nlp;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.StringReader;
import java.util.List;

public class EnglishParser {

    public EnglishParser() {

    }

    public ImmutableList<List<HasWord>> getSentences(String content) {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(content));
        return ImmutableList.copyOf(tokenizer.iterator());
    }
}
