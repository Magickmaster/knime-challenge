package com.jonassigel.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.jonassigel.AllowedType;
import com.jonassigel.Transform;
import com.jonassigel.Util;
import com.jonassigel.Transformers.Capitalize;
import com.jonassigel.Transformers.Negate;
import com.jonassigel.Transformers.NonNull;
import com.jonassigel.Transformers.Reverse;
import com.jonassigel.Transformers.Transformer;

import org.junit.Test;

public class TransformTest {
    @Test
    public void testGenerateTransformersFrom() {
        String[] valids = { "capitalize", "negate", "nonnull", "reverse" };
        List<Transformer> actuals = new ArrayList<>();
        actuals.add(Capitalize.getInstance());
        actuals.add(Negate.getInstance());
        actuals.add(NonNull.getInstance());
        actuals.add(Reverse.getInstance());

        List<Transformer> transformers = Transform.generateTransformersFrom(valids);
        assertArrayEquals(actuals.toArray(), transformers.toArray());

        try {
            Transform.generateTransformersFrom("capitalize", "wrong", "", "nuh");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testValidTransform() {
        Function<Object, Object> transformers = Util
                .compose(Transform.generateTransformersFrom("reverse", "negate").stream()
                        .map(t -> t.toTypedFunction(AllowedType.INT)));
        assertEquals((Integer) 1, transformers.apply(Integer.valueOf(-1)));

        transformers = Util
                .compose(Transform.generateTransformersFrom("negate").stream()
                        .map(t -> t.toTypedFunction(AllowedType.DOUBLE)));
        assertEquals((Double) 1.0, transformers.apply(Double.valueOf(-1.0)));

        transformers = Util
                .compose(Transform.generateTransformersFrom("reverse", "capitalize").stream()
                        .map(t -> t.toTypedFunction(AllowedType.STRING)));
        assertEquals("TSET", transformers.apply("Test"));
    }

    @Test
    public void testIllegalTransform() {
        List<Transformer> transformers = Transform.generateTransformersFrom("reverse", "negate");
        Function<Object, Object> func = Util
                .compose(transformers.stream().map(t -> t.toTypedFunction(AllowedType.STRING)));
        try {
            func.apply("Hello");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testTryWriteout() throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        OutputStreamWriter output = new OutputStreamWriter(result);
        Transform.tryConsume("Hello", output);
        output.flush();
        assertEquals("Hello\n", result.toString());
    }

    @Test
    public void testStreamTransform() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<Transformer> transformers = Transform.generateTransformersFrom("reverse");
        Stream<String> source = Arrays.asList("Hello", "World").stream();
        Transform.transformInput(AllowedType.STRING, output, transformers, source);
        assertEquals("olleH\ndlroW\n", output.toString());

    }

}
