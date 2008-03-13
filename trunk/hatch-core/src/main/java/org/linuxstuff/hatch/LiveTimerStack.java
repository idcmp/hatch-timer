package org.linuxstuff.hatch;

import org.linuxstuff.hatch.logger.BasicMetricsLogger;
import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

class LiveTimerStack implements TimerStack {

	private MetricsLoggerStrategy defaultMetricsLogger = BasicMetricsLogger.LOGGER;
	private TraceState state = TraceState.BY_REQUEST;

	private ThreadLocal<ThreadLocalMetricsBean> threadLocalMetrics = new ThreadLocal<ThreadLocalMetricsBean>();

	public void enableAndPush(String key) {

		if (state == TraceState.OFF) {
			return;
		}

		traceRequest();
		push(key);

	}

	public void pop(final String key) {
		pop(key, null);
	}

	public void pop(String key, final Long forcedDuration) {
		ThreadLocalMetricsBean metrics;

		if (state == TraceState.OFF) {
			return;
		}

		metrics = threadLocalMetrics.get();

		if (metrics == null || metrics.traceState == TraceState.OFF) {
			return;
		}

		if (metrics.currentElement == null) {
			metrics.getMetricsLogger().error(
					"TimerStackUtil.pop(\"" + key + "\") called without any prior calls to push().");
			return;
		}

		if (key.equals(metrics.currentElement.getName())) {

			if (forcedDuration == null) {
				metrics.currentElement.end();
			} else {
				metrics.currentElement.end(forcedDuration);

			}

			if (metrics.currentElement.getParent() == null) {
				metrics.logMetrics();

				threadLocalMetrics.set(null);
			} else {
				metrics.currentElement = metrics.currentElement.getParent();
			}
		} else {
			metrics.getMetricsLogger().error(
					"Pop of \"" + key + "\" called, when expecting \"" + metrics.currentElement.getName() + "\".");
			threadLocalMetrics.set(null); // reset thread's tracing state
		}

	}

	public void push(String key) {
		ThreadLocalMetricsBean metrics;

		switch (state) {

			case OFF:
				return;

				/**
				 * If we only push by request, then lets check if this thread
				 * has requested tracing.
				 */
			case BY_REQUEST:
				metrics = threadLocalMetrics.get();

				if (metrics == null || metrics.traceState == TraceState.OFF) {
					return;
				}

				metrics.push(new DurationBean(key));

				break;

			/**
			 * It's ON, we'll create a thread-specific stack if needed, or use
			 * the one we already have.
			 */
			case ON:
				metrics = threadLocalMetrics.get();
				if (metrics == null) {
					metrics = new ThreadLocalMetricsBean(state, new DurationBean(key), defaultMetricsLogger);

					threadLocalMetrics.set(metrics);

				} else {
					metrics.push(new DurationBean(key));
				}
				break;
		}

	}

	public void setMetricsLogger(MetricsLoggerStrategy logger) {

		ThreadLocalMetricsBean metrics;

		defaultMetricsLogger = logger;

		if (state == TraceState.OFF) {
			return;
		}

		metrics = threadLocalMetrics.get();

		if (metrics == null) {
			return;
		}

		metrics.setMetricsLogger(logger);

	}

	public void setState(TraceState state) {

		this.state = state;

		if (state == TraceState.OFF) {
			threadLocalMetrics.set(null);
		}

	}

	public void setThreadLocalMetricsLogger(MetricsLoggerStrategy logger) {
		ThreadLocalMetricsBean metrics;

		if (state == TraceState.OFF) {
			return;
		}

		metrics = threadLocalMetrics.get();

		if (metrics == null) {

			metrics = new ThreadLocalMetricsBean(state, null, logger);

			threadLocalMetrics.set(metrics);
		} else {

			metrics.setMetricsLogger(logger);

		}

	}

	/**
	 * Use to enable {@code BY_REQUEST} tracing for this thread. This value is
	 * reset once the last traced element is popped off the stack. Setting this
	 * multiple times before the stack is empty has no effect.
	 */
	private void traceRequest() {
		ThreadLocalMetricsBean metrics = threadLocalMetrics.get();

		if (metrics == null) {

			metrics = new ThreadLocalMetricsBean(defaultMetricsLogger);
			threadLocalMetrics.set(metrics);

		} else {

			metrics.traceState = TraceState.BY_REQUEST;

		}

	}

	public void reset() {
		threadLocalMetrics.set(null);
	}
}
