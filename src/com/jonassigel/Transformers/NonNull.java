package com.jonassigel.Transformers;

import java.util.Objects;

public class NonNull extends Transformer {

    private static final NonNull instance = new NonNull();

    @Override
    public String applyOnString(String t) {
        Objects.requireNonNull(t);
        return t;
    }

    @Override
    public Integer applyOnInteger(Integer t) {
        Objects.requireNonNull(t);
        return t;
    }

    @Override
    public Double applyOnDouble(Double t) {
        Objects.requireNonNull(t);
        return t;
    }

    public static NonNull getInstance() {
        return instance;
    }

}
