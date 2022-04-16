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
     * Takes a stream of inputs, transforms it and streams it to the output
     * 
     * @param type
     *            What type the incoming data has
     * @param target
     *            where the data should go to
     * @param transforms
     *            the operations to perform
     * @param elements
     *            the incoming data
     */
    public static void transformInput(AllowedType type, OutputStream target, List<Transformer> transforms,
            Stream<String> elements) {
        OutputStreamWriter consume = new OutputStreamWriter(target);
        Function<Object, Object> compiled = Util.compose(transforms.stream().map(t -> t.toTypedFunction(type)));
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
