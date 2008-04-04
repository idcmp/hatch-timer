package org.linuxstuff.hatch.logger;

import org.linuxstuff.hatch.DurationBean;

public interface MetricsLoggerStrategy {

	public void error(String message);

	public void logMetrics(DurationBean durationBean, long minimumThreshold);

}
