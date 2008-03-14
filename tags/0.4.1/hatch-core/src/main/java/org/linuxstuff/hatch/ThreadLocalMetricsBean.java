package org.linuxstuff.hatch;


import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;
import org.linuxstuff.hatch.logger.NullMetricsLogger;

/**
 * Composition of all thread-specific data required to be kept when tracing.
 * This bean knows if it has special requests for tracing or a specific logger
 * it should be using. It also keeps track of the elements in the stack.
 * 
 * @author idcmp
 */

final class ThreadLocalMetricsBean {

	DurationBean currentElement;
	private MetricsLoggerStrategy metricsLogger;
	TraceState traceState;

	/**
	 * Constructor used only when enabling {@code TraceState#BY_REQUEST}
	 * tracing.
	 */
	protected ThreadLocalMetricsBean(MetricsLoggerStrategy logger) {
		this.traceState = TraceState.BY_REQUEST;
		this.currentElement = null;
		setMetricsLogger(logger);
	}

	protected ThreadLocalMetricsBean(TraceState traceState, DurationBean outerDuration, MetricsLoggerStrategy logger) {
		this.traceState = traceState;
		this.currentElement = outerDuration;
		this.metricsLogger = logger;
	}

	public MetricsLoggerStrategy getMetricsLogger() {
		return metricsLogger;
	}

	void logMetrics() {
		metricsLogger.logMetrics(currentElement);
	}

	void push(DurationBean bean) {

		if (this.currentElement != null) {
			this.currentElement.addChild(bean);
		}

		this.currentElement = bean;
	}

	public void setMetricsLogger(MetricsLoggerStrategy logger) {
		if (logger == null) {
			this.metricsLogger = NullMetricsLogger.LOGGER;
		} else {
			this.metricsLogger = logger;
		}
	}

	@Override public String toString() {
		return "ThreadLocalMetricsBean [" + traceState + " logger=" + metricsLogger + " current=" + currentElement
				+ "]";
	}
}
