package com.jonassigel.Transformers;

import java.util.Objects;

/**
 * Makes strings into UPPER CASE
 */
public class Capitalize extends Transformer {

    private static final Capitalize instance = new Capitalize();;

    /**
     * Transforms the input into UPPER CASE
     */
    @Override
    public String applyOnString(String t) {
        Objects.requireNonNull(t);
        return t.toUpperCase();
    }

    public static Capitalize getInstance() {
        return instance;
    }

}
