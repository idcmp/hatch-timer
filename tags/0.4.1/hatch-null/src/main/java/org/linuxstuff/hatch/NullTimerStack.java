package org.linuxstuff.hatch;

import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

/**
 * This a null object version of a TimerStack. This is used for the
 * <b>hatch-null</b> implementation. By using the hatch-null implementation you
 * can fully disable Hatch at compile time. Not that I recommend doing that.
 * 
 * @author idcmp
 * 
 */
class NullTimerStack implements TimerStack {

	public void enableAndPush(String key) {
		// null object, do nothing
	}

	public void pop(String key) {
		// null object, do nothing
	}

	public void pop(String key, Long forcedDuration) {
		// null object, do nothing
	}

	public void push(String key) {
		// null object, do nothing
	}

	public void setMetricsLogger(MetricsLoggerStrategy logger) {
		// null object, do nothing
	}

	public void setState(TraceState state) {
		// null object, do nothing
	}

	public void setThreadLocalMetricsLogger(MetricsLoggerStrategy logger) {
		// null object, do nothing
	}
	
	public void reset() {
		// null object, do nothing
	}
}
