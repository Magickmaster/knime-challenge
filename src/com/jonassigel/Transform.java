package com.jonassigel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jonassigel.Transformers.Transformer;

public class Transform {

    public static List<Transformer> generateTransformersFrom(String... tokens) {
        return Arrays.stream(tokens)
                .filter(s -> Objects.nonNull(s) && !s.isBlank()).map(String::trim).map(Transformer::toTransformer)
                .collect(Collectors.toList());
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

    static void transformInput(String type, OutputStream target, List<Transformer> transformers,
            Stream<String> elements)
            throws IOException {
        OutputStreamWriter consume = new OutputStreamWriter(target);

        elements = elements.peek(s -> Statistics.getInstance().updateStatisticsWithLine(s));
        switch (type) {
            case "int":
                elements.map(Integer::valueOf).map(i -> Transform.transform(i, transformers))
                        .forEachOrdered(r -> tryConsume(r, consume));
                break;
            case "double":
                elements.map(Double::valueOf).map(i -> Transform.transform(i, transformers))
                        .forEachOrdered(r -> tryConsume(r, consume));
                break;
            default:
                elements.map(i -> Transform.transform(i, transformers))
                        .forEachOrdered(r -> tryConsume(r, consume));
        }
        target.flush();
        consume.close();
    }

    public static void tryConsume(Object toConsume, OutputStreamWriter output) {
        Objects.requireNonNull(toConsume);
        Objects.requireNonNull(output);

        try {
            output.append(toConsume.toString() + "\n");

        } catch (IOException e) {
            System.err.println("A value could not be written to the desired output");
        }
    }
}
