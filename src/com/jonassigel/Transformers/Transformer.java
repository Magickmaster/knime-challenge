package com.jonassigel.Transformers;

public abstract class Transformer {

    public String applyOnString(String t) {
        throw new IllegalArgumentException("Type not applicable for " + this.getClass().getSimpleName());
    }

    public Integer applyOnInteger(Integer t) {
        throw new IllegalArgumentException("Type not applicable for " + this.getClass().getSimpleName());
    }

    public Double applyOnDouble(Double t) {
        throw new IllegalArgumentException("Type not applicable for " + this.getClass().getSimpleName());
    }

    public static Transformer getInstance() throws Exception {
        throw new IllegalAccessException("Do not touch the Transformer, it is shy!");
    }

    public static Transformer toTransformer(String identifier) {
        switch (identifier.toLowerCase()) {
            case "capitalize":
                return Capitalize.getInstance();
            case "reverse":
                return Reverser.getInstance();
            case "nonnull":
                return NonNull.getInstance();
            case "negate":
                return Negate.getInstance();
            default:
                throw new IllegalArgumentException(
                        "The supplied string is not a valid transformer! Offender: {" + identifier + "}");
        }
    }
}
