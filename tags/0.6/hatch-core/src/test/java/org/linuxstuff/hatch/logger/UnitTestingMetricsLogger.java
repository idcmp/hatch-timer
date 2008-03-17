package org.linuxstuff.hatch.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.linuxstuff.hatch.DurationBean;

public class UnitTestingMetricsLogger extends BasicMetricsLogger implements MetricsLoggerStrategy {

	private static final String newline = System.getProperty("line.separator");
	public List<String> errors = new ArrayList<String>();

	public Map<DurationBean, String> logRequests = new TreeMap<DurationBean, String>();

	private String lastDump = null;

	public void error(String message) {
		errors.add(message);
	}

	public void logMetrics(DurationBean durationBean,long duration) {

		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

		logMetric(new PrintStream(byteArrayStream), "", durationBean,0);

		this.lastDump = byteArrayStream.toString();
		logRequests.put(durationBean, this.lastDump);
	}

	/**
	 * Count the number of lines as returned from a call to
	 * {@code BasicMetricsLogger#logMetric(PrintStream, String, DurationBean)}.
	 */
	public int linesInLasttTrace() {
		int count = 0, offset = -1;

		for (;;) {
			offset = lastDump.indexOf(newline, offset + 1);

			if (offset == -1) {
				return count;
			}
			count++;
		}

	}

	public String getLastDump() {
		return this.lastDump;
	}

}
