package org.linuxstuff.hatch.logger;

import java.io.PrintStream;

import org.linuxstuff.hatch.DurationBean;

public class BasicMetricsLogger implements MetricsLoggerStrategy {

	public static final MetricsLoggerStrategy LOGGER = new BasicMetricsLogger();

	/**
	 * Constructor used for subclassing only. Use
	 * {@code BasicMetricLogger#LOGGER}, for your code.
	 */
	BasicMetricsLogger() {
		// For subclassing, see ExceedsDurationMetricLogger.
	}

	/**
	 * Dump the error and a copy of the current stack trace to
	 * {@code System.err}.
	 */
	public void error(String message) {
		System.err.println("Error: " + message);
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
			System.err.println(element.toString());
		}
	}

	protected void logMetric(PrintStream out, String indent, DurationBean bean) {
		out.println(indent + "[" + bean.getDuration() + "ms] " + bean.getName());
		for (DurationBean child : bean.getChildren()) {
			logMetric(out, indent + "  ", child);
		}
	}

	public void logMetrics(DurationBean durationBean) {
		logMetric(System.out, "", durationBean);
	}

}
