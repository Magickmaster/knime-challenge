package com.jonassigel.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
        Transform.transform(Integer.valueOf(-1), transformers);
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

}
