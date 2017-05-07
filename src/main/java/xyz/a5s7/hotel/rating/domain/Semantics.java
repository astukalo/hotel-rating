package xyz.a5s7.hotel.rating.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Semantics {
    private List<PhraseAdd> positive;
    private List<PhraseAdd> negative;
    private List<PhraseMult> intensifier;

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
}
