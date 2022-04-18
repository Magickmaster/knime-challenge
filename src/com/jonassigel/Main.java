package com.jonassigel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
	 * Takes data from file, transforms it with a series of operations and writes
	 * the data out to some specified location
	 * 
	 * @param args
	 *            The arguments as per the exercise PDF
	 * @throws IOException
	 */
	public static void main(String[] args) {
		// add your code here

		// I do not like to assume that the arguments are correct but i'd implement a
		// large framework just for that. Which I won't.
		// EDIT: I did. (Leaving these comments for comedic effect)

		// Unwrap the supplied arguments
		Set<String> arguments = Arguments.getPresentArguments(args, flags);
		Map<String, String> argPairs = Arguments.getPariedArguments(args, flags);

		if (arguments.isEmpty()) {
			System.err.println("No arguments have been supplied! Exiting!");
			System.exit(1);
		}
		// Extract and map necessary information
		String typeString = argPairs.get("--inputtype").toUpperCase();

		AllowedType type;
		try {
			type = Enum.valueOf(AllowedType.class, typeString);
		} catch (IllegalArgumentException e) {
			System.err.println("The supplied type " + typeString + " is not supported. Fallback is String.");
			type = AllowedType.STRING;
		}

		String[] ops = argPairs.get("--operations").split(",");
		List<Transformer> transformers = Transform.generateTransformersFrom(ops);

		// Prepare output methodology
		OutputStream target = System.out;
		try {
			if (arguments.contains("--output")) {
				target = new FileOutputStream(argPairs.get("--output"));
			}
		} catch (IOException e) {
			System.err.println("Output not possible: " + e);
			System.err.println("Fallback to sysout! ");
			target = System.out;
		}
		// Open file and allow asynchronous operation on data
		try (Scanner sc = new Scanner(Paths.get(argPairs.get("--input")))) {
			sc.useDelimiter("\n");
			// Custom stream.parallel could be implemented as per part #3
			// Arguments why it is bad here: Input can be very very long, or even infinite
			// (networked streaming microservices anyone?)
			// Putting this in an asynchronous worker with the indefinite lazy input stream
			// could be powerful. If the parallelization requires the data to be
			// well-defined and complete beforehand, this is impossible.
			// Proof of thesis: Extremely long input is processed mostly out of memory and
			// very fast.
			// Test file: 35GB text, memory usage never over 8gb, completed successfully
			// with parallel usage. Other method would have required loading all to memory,
			// which is not possible with just 16gb +8 swap ram
			System.out.println("");
			System.out.println("");
			OutputStreamWriter output = new OutputStreamWriter(target);
			Stream<String> elements = sc.tokens().parallel();
			Transform.transformInput(type, output, transformers, elements);
			System.out.println("");
			System.out.println("");
		} catch (IOException e) {
			// Wish: Logging library
			System.err.println("There was an IO exception: " + e.getMessage());
			System.exit(1);
		}
		// DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.println(String.format("Processed %d lines (%d of which were unique)", //
				Statistics.getInstance().getNoOfLinesRead(), //
				Statistics.getInstance().getNoOfUniqueLines()));
	}

}
