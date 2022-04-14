package com.jonassigel.Transformers;

import java.util.Objects;

/**
 * Reverses a compatible input
 */
public class Reverse extends Transformer {

    // Lightweight object, small project.
    private static final Reverse instance = new Reverse();

    /**
     * Reverses the string so the chars are arranged backwards
     */
    @Override
    public String applyOnString(String t) {
        Objects.requireNonNull(t);
        char[] result = new char[t.length()];
        for (int i = 0; i < t.length(); i++) {
            result[i] = t.charAt(t.length() - i - 1);
        }
        return String.valueOf(result);
    }

    /**
     * Reverses the integer so its decimals are arranged backwards, with negative
     * numbers retaining the '-' prefix
     */
    @Override
    public Integer applyOnInteger(Integer t) {
        Objects.requireNonNull(t);

        // Can't reverse negatives because you can't parse "1-"
        if (t < 0) {
            t = Negate.getInstance().applyOnInteger(t);
            t = Integer.parseInt(this.applyOnString(Integer.toString(t)));
            return Negate.getInstance().applyOnInteger(t);
        }

        return Integer.parseInt(this.applyOnString(Integer.toString(t)));
    }

    public static Reverse getInstance() {
        return instance;
    }

}
