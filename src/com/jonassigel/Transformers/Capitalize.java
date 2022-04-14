package com.jonassigel.Transformers;

import java.util.Objects;

public class Capitalize extends Transformer {

    private static final Capitalize instance = new Capitalize();;

    @Override
    public String applyOnString(String t) {
        Objects.requireNonNull(t);
        return t.toUpperCase();
    }

    public static Capitalize getInstance() {
        return instance;
    }

}
