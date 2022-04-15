package com.jonassigel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jonassigel.Transformers.Transformer;

public class Transform {

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input
     *            The target
     * @param transformers
     *            The list of operations to perform in order
     * @return The result of all applications
     */
    public static Object transform(Object input, List<Transformer> transformers) {
        throw new IllegalArgumentException("The supplied type doesn't have a pipeline yet");
    }

    /**
     * Transforms a given input depending on its type through the list of
     * transformers. Each type needs to have its own instance sendoff
     * 
     * @param input
     *            The target
     * @param transformers
     *            The list of operations to perform in order
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
     * @param input
     *            The target
     * @param transformers
     *            The list of operations to perform in order
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
     * @param input
     *            The target
     * @param transformers
     *            The list of operations to perform in order
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
     * @param type
     *            What type the incoming data has
     * @param target
     *            where the data should go to
     * @param transformers
     *            the operations to perform
     * @param elements
     *            the incoming data
     * @throws IOException
     *             if
     */
    public static void transformInput(AllowedTypes type, OutputStream target, List<Transformer> transformers,
            Stream<String> elements) {
        OutputStreamWriter consume = new OutputStreamWriter(target);
        Function<Object, Object> compiled = Util.compose(transformers.stream().map(t -> t.toTypedFunction(type)));
        elements = elements.peek(s -> Statistics.getInstance().updateStatisticsWithLine(s));
        elements.map(type.stringCaster).map(compiled).forEachOrdered(r -> tryConsume(r, consume));
        try {
            target.flush();
            consume.close();
        } catch (IOException e) {
            System.err.println("Could not cleanup outputs after all operations: " + e.getMessage());
        }
    }

    /**
     * Fetches the respective transformers for each entry token
     * 
     * @param tokens
     *            The tokens of the operations
     * @return The transformers in order
     */
    public static List<Transformer> generateTransformersFrom(String... tokens) {
        return Arrays.stream(tokens)
                .filter(s -> Objects.nonNull(s) && !s.isBlank())
                .map(String::trim)
                .map(Transformer::toTransformer)
                .collect(Collectors.toList());
    }

    /**
     * Attempts to append the transformed data into the output.
     * 
     * @param toConsume
     *            the generated element
     * @param output
     *            the upstream
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
