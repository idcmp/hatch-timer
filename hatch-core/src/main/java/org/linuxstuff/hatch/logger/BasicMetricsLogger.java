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

	public void error(String message) {
		System.err.println(message);
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
