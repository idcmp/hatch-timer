package org.linuxstuff.hatch.logger;

import org.linuxstuff.hatch.DurationBean;

/**
 * A logger that extends {@code BasicMetricLogger}, but will only log stack
 * output if the duration of top of the stack is greater than a specified
 * threshold (in milliseconds).
 * 
 * @author idcmp
 * 
 */
public class ExceedsDurationMetricsLogger extends BasicMetricsLogger implements MetricsLoggerStrategy {

	private long thresholdDuration;

	public ExceedsDurationMetricsLogger(long thresholdDuration) {
		this.thresholdDuration = thresholdDuration;
	}

	@Override public void logMetrics(DurationBean durationBean) {
		if (durationBean.getDuration() > thresholdDuration) {
			super.logMetrics(durationBean);
		}
	}

}
