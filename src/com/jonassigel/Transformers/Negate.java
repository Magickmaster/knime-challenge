package com.jonassigel.Transformers;

import java.util.Objects;

/**
 * Function to negate an input.
 * Sadly, typing issues require a new implementation for each Number type. This
 * is to allow instatiated Number types to remain purity and would require many
 * waring flags.
 */
public class Negate extends Transformer {

    private static final Negate instance = new Negate();

    /**
     * Mathematically negates a number
     */
    @Override
    public Integer applyOnInteger(Integer t) {
        Objects.requireNonNull(t);
        return t * -1;
    }

    /**
     * Mathematically negates a number
     */
    @Override
    public Double applyOnDouble(Double t) {
        Objects.requireNonNull(t);
        return t * -1;
    }

    public static Negate getInstance() {
        return instance;
    }

}
