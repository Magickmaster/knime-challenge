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

    /**
     * Fetches the respective transformers for each entry token
     * 
     * @param tokens The tokens of the operations
     * @return The transformers in order
     */
    public static List<Transformer> generateTransformersFrom(String... tokens) {
        return Arrays.stream(tokens)
                .filter(s -> Objects.nonNull(s) && !s.isBlank()).map(String::trim).map(Transformer::toTransformer)
                .collect(Collectors.toList());
    }

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input        The target
     * @param transformers The list of operations to perform in order
     * @return The result of all applications
     */
    public static Object transform(Object input, List<Transformer> transformers) {
        throw new IllegalArgumentException("The supplied type doesn't have a pipeline yet");
    }

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input        The target
     * @param transformers The list of operations to perform in order
     * @return The result of all applications
     */
    public static Double transform(Double d, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            d = t.applyOnDouble(d);
        }
        return d;
    }

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input        The target
     * @param transformers The list of operations to perform in order
     * @return The result of all applications
     */
    public static String transform(String s, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            s = t.applyOnString(s);
        }
        return s;
    }

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input        The target
     * @param transformers The list of operations to perform in order
     * @return The result of all applications
     */
    public static Integer transform(Integer i, List<Transformer> transformers) {
        for (Transformer t : transformers) {
            i = t.applyOnInteger(i);
        }
        return i;
    }

    /**
     * Takes a stream of inputs, transforms it and streams it to the output
     * 
     * @param type         What type the incoming data has
     * @param target       where the data should go to
     * @param transformers the operations to perform
     * @param elements     the incoming data
     * @throws IOException if
     */
    static void transformInput(String type, OutputStream target, List<Transformer> transformers,
            Stream<String> elements) {
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
            default: // Remain String
                elements.map(i -> Transform.transform(i, transformers))
                        .forEachOrdered(r -> tryConsume(r, consume));
        }
        try {

            target.flush();
            consume.close();

        } catch (IOException e) {
            System.err.println("Could not cleanup outputs after all operations: " + e.getMessage());
        }
    }

    /**
     * Attempts to append the transformed data into the output.
     * 
     * @param toConsume the generated element
     * @param output    the upstream
     */
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
