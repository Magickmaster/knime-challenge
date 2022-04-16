package com.jonassigel.Transformers;

import java.util.function.Function;

import com.jonassigel.AllowedType;

/**
 * Declares data types that can be transformed.
 * Operations that do not have a compatible implementation automatically throw
 * exceptions.
 * 
 * If an operation wants to be capable for a type, it can override the specific
 * method. (Sadly type erasure destroyed another concept)
 * 
 * To add new types: Add an entry in AllowedType enum, add corresponding parent
 * function and an entry for the typed function
 */
public abstract class Transformer {

    public Object apply(Object t) {
        throw new IllegalArgumentException("Type not applicable for " + this.getClass().getSimpleName());
    }

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
     * 
     * @param identifier
     *            The string identifier of a transformer
     * @return
     */
    public static Transformer toTransformer(String identifier) {
        return switch (identifier.toLowerCase()) {
            case "capitalize" -> Capitalize.getInstance();
            case "reverse" -> Reverse.getInstance();
            case "nonnull" -> NonNull.getInstance();
            case "negate" -> Negate.getInstance();
            default -> throw new IllegalArgumentException(
                    "The supplied string is not a valid transformer! Offender: {" + identifier + "}");
        };
    }

    public Function<Object, Object> toTypedFunction(AllowedType type) {
        return switch (type) {
            case INT -> (x) -> applyOnInteger((Integer) x);
            case DOUBLE -> (x) -> applyOnDouble((Double) x);
            default -> (x) -> applyOnString((String) x);
        };
    }
}
