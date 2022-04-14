package com.jonassigel.Transformers;

/**
 * Declares data types that can be transformed.
 * Operations that do not have a compatible implementation automatically throw
 * exceptions.
 * 
 * If an operation wants to be capable for a type, it can override the specific
 * method. (Sadly type erasure destroyed another concept)
 */
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

    /**
     * The transformer does not have an instance itself
     * 
     * @throws IllegalAccessException
     */
    public static Transformer getInstance() throws IllegalAccessException {
        throw new IllegalAccessException("Do not touch the Transformer, it is shy!");
    }

    /**
     * Retrieves a transformer instance by its name.
     * Transformers can be acquired by their instance as well.
     * @param identifier The string identifier of a transformer
     * @return
     */
    public static Transformer toTransformer(String identifier) {
        switch (identifier.toLowerCase()) {
            case "capitalize":
                return Capitalize.getInstance();
            case "reverse":
                return Reverse.getInstance();
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
