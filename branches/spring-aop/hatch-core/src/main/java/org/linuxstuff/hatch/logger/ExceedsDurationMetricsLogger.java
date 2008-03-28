package org.linuxstuff.hatch.logger;

import org.linuxstuff.hatch.DurationBean;

/**
 * A logger that implements {@code MetricsLoggerStrategy}, but will only log
 * stack output if the duration of top of the stack is greater than a specified
 * threshold (in milliseconds). It will delegate the logging to the one passed
 * in during construction.
 * 
 * @author idcmp
 * 
 */
public class ExceedsDurationMetricsLogger implements MetricsLoggerStrategy {

	private long thresholdDuration;
	private MetricsLoggerStrategy delegate;

	public ExceedsDurationMetricsLogger(MetricsLoggerStrategy delegate,
			long thresholdDuration) {
		this.delegate = delegate;
		this.thresholdDuration = thresholdDuration;
	}

	public void logMetrics(DurationBean durationBean, long minimumDuration) {
		if (durationBean.getDuration() > thresholdDuration) {
			delegate.logMetrics(durationBean, minimumDuration);
		}
	}

	public void error(String message) {
		delegate.error(message);
	}

}
