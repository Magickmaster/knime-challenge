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
import java.util.stream.Stream;

import com.jonassigel.AllowedTypes;
import com.jonassigel.Transform;
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
        List<Transformer> transformers = Transform.generateTransformersFrom("reverse", "negate");
        assertEquals((Integer) 1, Transform.transform(Integer.valueOf(-1), transformers));

        transformers = Transform.generateTransformersFrom("negate");
        assertEquals((Double) 1.0, Transform.transform(Double.valueOf(-1.0), transformers));

        transformers = Transform.generateTransformersFrom("reverse", "capitalize");
        assertEquals("TSET", Transform.transform("test", transformers));
    }

    @Test
    public void testMultitypeTransform() {
        List<Transformer> transformers = Transform.generateTransformersFrom("reverse");
        assertEquals("tset", Transform.transform("test", transformers));
        assertEquals(Integer.valueOf(1), Transform.transform(Integer.valueOf(1), transformers));
    }

    @Test
    public void testIllegalTransform() {
        List<Transformer> transformers = Transform.generateTransformersFrom("reverse", "negate");
        try {
            Transform.transform("Hello", transformers);
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
        Transform.transformInput(AllowedTypes.STRING, output, transformers, source);
        assertEquals("olleH\ndlroW\n", output.toString());

    }

}
