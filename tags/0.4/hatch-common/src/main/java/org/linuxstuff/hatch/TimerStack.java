package org.linuxstuff.hatch;

import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

/**
 * The necessary methods to implement a timer stack. This is used by the live
 * implementation in the normal use of Hatch, and in the null-pattern version of
 * <b>hatch-null</b>. See TimerStackUtil in <b>hatch-core</b> for usage
 * information of each method.
 * 
 * @author idcmp
 * 
 */
interface TimerStack {

	public void enableAndPush(final String key);

	public void pop(final String key);

	public void pop(final String key, final Long forcedDuration);

	public void push(final String key);

	public void setMetricsLogger(MetricsLoggerStrategy logger);

	public void setState(TraceState state);

	public void setThreadLocalMetricsLogger(MetricsLoggerStrategy logger);
	
	public void reset();

}
