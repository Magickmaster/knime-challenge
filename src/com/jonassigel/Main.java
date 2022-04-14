package com.jonassigel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
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

	/**
	 * A set of supported flags
	 */
	static final String[] flags = { "--input", "--inputtype", "--operations", "--threads", "--output" };

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// add your code here

		// I do not like to assume that the arguments are correct but i'd implement a
		// large framework just for that. Which I won't.
		// EDIT: I did. (Leaving these comments for comedic effect)

		// Unwrap the supplied arguments
		Set<String> arguments = Arguments.getPresentArguments(args, flags);
		Map<String, String> argPairs = Arguments.getPariedArguments(args, flags);

		// Extract and map necessary information
		String type = argPairs.get("--inputtype").toLowerCase();
		String[] ops = argPairs.get("--operations").split(",");
		List<Transformer> transformers = Transform.generateTransformersFrom(ops);

		// Prepare output methodology
		OutputStream target = System.out;
		if (arguments.contains("--output")) {
			System.out.println(argPairs.get("--output"));
			Files.createFile(Path.of(argPairs.get("--output")));
			target = new FileOutputStream(argPairs.get("--output"));
		}

		// Open file and allow asynchronous operation on data
		try (Scanner sc = new Scanner(Paths.get(argPairs.get("--input")))) {
			sc.useDelimiter("\n");
			// Custom stream.parallel could be implemented as per part #3
			// Arguments why it is bad here: Input can be very very long, or even infinite
			// (networked streaming microservices anyone?)
			// Putting this in an asynchronous worker with the indefinite lazy input stream
			// could be powerful. If the parallelization requires the data to be
			// well-defined
			// and complete beforehand, this is impossible.
			// Proof of thesis: Extremely long input
			Stream<String> elements = sc.tokens().parallel();
			Transform.transformInput(type, target, transformers, elements);
		} catch (IOException e) {
			System.err.println("There was an IO exception: " + e.getMessage());
		}

		// DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.println(String.format("Processed %d lines (%d of which were unique)", //
				Statistics.getInstance().getNoOfLinesRead(), //
				Statistics.getInstance().getNoOfUniqueLines()));
	}

}
