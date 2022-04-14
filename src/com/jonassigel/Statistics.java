package com.jonassigel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Captures statistics about the lines being read from the input file.
 * Thread-safe accumulation.
 * 
 * @author KNIME GmbH
 */
public class Statistics {

	private final Set<String> linesRead = ConcurrentHashMap.newKeySet();

	private LongAccumulator lineCounter = new LongAccumulator((x, y) -> x + y, 0);

	private static Statistics instance = null;

	/**
	 * Updates statistics with respect to the given line. This method is supposed to
	 * be called when a new line has been read from the input file.
	 * 
	 * @param line
	 *             A new line that has been read from the input file.
	 */
	public void updateStatisticsWithLine(final String line) {
		lineCounter.accumulate(1);
		linesRead.add(line);
	}

	/**
	 * 
	 * @return the total number of lines read.
	 */
	public int getNoOfLinesRead() {
		return lineCounter.intValue();
	}

	/**
	 * 
	 * @return the number of unique lines read.
	 */
	public int getNoOfUniqueLines() {
		return linesRead.size();
	}

	/**
	 * 
	 * @return the shared {@link Statistics} instance to use.
	 */
	public static Statistics getInstance() {
		if (instance == null) {
			// synchronized tatas to prevent race condition. Wish there was a nicer way.
			synchronized (Statistics.class) {
				if (instance == null)
					instance = new Statistics();
			}
		}
		return instance;
	}
}
