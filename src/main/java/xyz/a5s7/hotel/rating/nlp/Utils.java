package xyz.a5s7.hotel.rating.nlp;

import edu.stanford.nlp.ling.HasWord;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String asString(List<? extends HasWord> sentence) {
        return sentence.stream()
                .map(i -> i.word())
                .collect(Collectors.joining(" "));
    }
}
