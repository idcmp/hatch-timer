package org.linuxstuff.hatch;

import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

public class TimerStackUtil {

	private static final TimerStack timerStack = new NullTimerStack();

	public static void enableAndPush(final String key) {

		timerStack.enableAndPush(key);
	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void pop(final String key) {
		timerStack.pop(key);

	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void pop(final String key, final Long forcedDuration) {
		timerStack.pop(key, forcedDuration);
	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void push(final String key) {
		timerStack.push(key);
	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void setMetricsLogger(MetricsLoggerStrategy logger) {
		timerStack.setMetricsLogger(logger);
	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void setState(TraceState state) {
		timerStack.setState(state);
	}

	/**
	 * You are using the <b>hatch-null</b> implementation of Hatch. All calls
	 * are no-ops. You should use the <b>hatch-core</b> artifact if you want
	 * Hatch to do something.
	 */
	public static void setThreadLocalMetricsLogger(MetricsLoggerStrategy logger) {
		timerStack.setThreadLocalMetricsLogger(logger);
	}

}
