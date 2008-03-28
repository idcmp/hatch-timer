package org.linuxstuff.hatch.logger;

import org.linuxstuff.hatch.DurationBean;

public class NullMetricsLogger implements MetricsLoggerStrategy {

	public static final NullMetricsLogger LOGGER = new NullMetricsLogger();

	public void error(String message) {
		// do nothing!

	}

	public void logMetrics(DurationBean durationBean, long minimumDuration) {
		// also do nothing!
	}

}
