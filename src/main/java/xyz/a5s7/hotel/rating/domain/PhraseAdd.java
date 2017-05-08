package xyz.a5s7.hotel.rating.domain;

public class PhraseAdd {
    private String phrase;
    private float value;

    public PhraseAdd() {
    }

    public PhraseAdd(final String phrase, final float value) {
        this.phrase = phrase;
        this.value = value;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PhraseAdd phraseAdd = (PhraseAdd) o;

        if (Float.compare(phraseAdd.value, value) != 0) return false;
        return phrase != null ? phrase.equals(phraseAdd.phrase) : phraseAdd.phrase == null;
    }

    @Override
    public int hashCode() {
        int result = phrase != null ? phrase.hashCode() : 0;
        result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
        return result;
    }

    @Override
    public String toString() {
        return phrase + " (" + value + ")";
    }
}
