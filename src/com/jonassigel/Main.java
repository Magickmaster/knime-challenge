package com.jonassigel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import com.jonassigel.Transformers.Transformer;
import com.jonassigel.arguments.Arguments;

/**
 * Main class.
 * 
 * @author Jonas Sigel for KNIME GmbH
 */
public class Main {
	static Writer output;
	static String[] flags = { "--input", "--inputtype", "--operations", "--threads", "--output" };

	public static void main(String[] args) throws IOException {

		// add your code here

		// I do not like to assume that the arguments are correct but i'd implement a
		// large framework just for that. Which I won't.
		// EDIT: I did. (Leaving these comments for comedic effect)
		Set<String> arguments = Arguments.getPresentArguments(args, flags);
		Map<String, String> argPairs = Arguments.getPariedArguments(args, flags);

		argPairs.put("--operations", "capitalize,reverse");

		List<Transformer> transformers = Transform.generateTransformersFrom(argPairs.get("--operations").split(","));
		try (Scanner sc = new Scanner("Hello World!")) {

			sc.useDelimiter("\n");
			Stream<String> elements = sc.tokens().parallel();
			OutputStreamWriter consume;
			if (arguments.contains("--output")) {
				consume = new OutputStreamWriter(new FileOutputStream(argPairs.get("--output")));

			} else {
				consume = new OutputStreamWriter(System.out);
			}
			elements = elements.peek(s -> Statistics.getInstance().updateStatisticsWithLine(s));
			switch (argPairs.get("--inputtype").toLowerCase()) {
				case "integer":
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

			System.out.println();
			System.out.println("Transformers:");
		}

		// DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.println(String.format("Processed %d lines (%d of which were unique)", //
				Statistics.getInstance().getNoOfLinesRead(), //
				Statistics.getInstance().getNoOfUniqueLines()));
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
