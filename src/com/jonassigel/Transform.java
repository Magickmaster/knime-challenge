package com.jonassigel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jonassigel.Transformers.Transformer;

public class Transform {

    public static List<Transformer> generateTransformersFrom(String... tokens) {
        return Arrays.stream(tokens)
                .map(String::trim).map(Transformer::toTransformer).collect(Collectors.toList());
    }

    public static Object transform(Object input, List<Transformer> transformers) {
        if (input instanceof String s) {
            for (Transformer t : transformers) {
                s = t.applyOnString(s);
            }
            return s;
        }
        if (input instanceof Integer i) {
            for (Transformer t : transformers) {
                i = t.applyOnInteger(i);
            }
            return i;
        }
        if (input instanceof Double d) {
            for (Transformer t : transformers) {
                d = t.applyOnDouble(d);
            }
            return d;
        }
        throw new IllegalArgumentException("The supplied type doesn't have a pipeline yet");
    }

    public static String transformString(String input, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            input = t.applyOnString(input);
        }
        return input;
    }

    public static Object transformInteger(Integer input, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            input = t.applyOnInteger(input);
        }
        return input;
    }

    public static Object transformDouble(Double input, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            input = t.applyOnDouble(input);
        }
        return input;
    }
}
