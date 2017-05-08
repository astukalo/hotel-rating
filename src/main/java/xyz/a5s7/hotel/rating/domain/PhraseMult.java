package xyz.a5s7.hotel.rating.domain;

public class PhraseMult {
    private String phrase;
    private float multiplier;

    public PhraseMult(final String phrase, final float multiplier) {
        this.phrase = phrase;
        this.multiplier = multiplier;
    }

    public PhraseMult() {
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PhraseMult that = (PhraseMult) o;

        if (Float.compare(that.multiplier, multiplier) != 0) return false;
        return phrase != null ? phrase.equals(that.phrase) : that.phrase == null;
    }

    @Override
    public int hashCode() {
        int result = phrase != null ? phrase.hashCode() : 0;
        result = 31 * result + (multiplier != +0.0f ? Float.floatToIntBits(multiplier) : 0);
        return result;
    }

    @Override
    public String toString() {
        return phrase + " (" + multiplier + ")";
    }
}
