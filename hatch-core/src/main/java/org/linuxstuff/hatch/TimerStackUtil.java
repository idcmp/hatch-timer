package org.linuxstuff.hatch;

import org.linuxstuff.hatch.logger.ExceedsDurationMetricsLogger;
import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

/**
 * A stack-based performance logger. This can be used to break down where your
 * application is spending its time. There is a slight overhead for enabling
 * tracing and the minimum granularity is 1ms making this more ideal when code
 * interfaces with externals systems.
 * 
 * By default, tracing happens by request. Calling
 * {@code TimerStack#enableAndPush(String)} will enable tracing for the current
 * thread until a corresponding pop() call is received.
 * 
 * Tracing can be enabled 'globally' by calling
 * <code>TimerStack.setState(TraceState.ON)</code>.
 * 
 * Note: Callers should strive to ensure that push/pop pairs match. Generally,
 * this can be accomplished by including the pop() call in a finally block.
 * 
 * Simple Use:
 * 
 * <code>
 * 
 *    TimerStack.push("add-account");
 *    ...
 *    TimerStack.pop("add-account");
 * 
 * </code>
 *  
 * @see TimerStack#push(String)
 * @see TimerStack#pop(String)
 * 
 * 
 * This class is actually a proxy to a {@code LiveTimerStack} implementation.  You can swap out this
 * live TimerStackUtil by using the <b>hatch-null</b> artifact.  It has a separate TimerStackUtil
 * implementation but proxies to a null object implementation.
 * 
 * @author idcmp
 * 
 */

public class TimerStackUtil {

	private static TimerStack timerStack = new LiveTimerStack();
	
	/**
	 * Enable {@code TraceState#BY_REQUEST} and push the given key onto the
	 * timer stack. The BY_REQUEST state of the stack will remain on the current
	 * thread until the last element is popped off the stack. <b>NOTE:</b> This
	 * method will not enable tracing if {@code TimerStack#state} is
	 * {@code TraceState#OFF}.
	 * 
	 * @see #push(String);
	 */
	public static void enableAndPush(final String key) {
		
		timerStack.enableAndPush(key);
	}

	/**
	 * Use to pop a named element off the timer stack. This element is
	 * identified by {@code key}. This method does nothing if the timer stack
	 * state is {@code TraceState#OFF} or if nothing has yet been pushed onto
	 * the stack. If the last element of the stack is popped, then this triggers
	 * the stack to be dumped and all thread local data is reset.
	 * 
	 * If you want to test push/pop, you may be interested in
	 * {@code TimerStack#pop(String, Long)}, that allows you to handcraft a
	 * fake duration into the stack.
	 * 
	 * @see TimerStack#pop(String, Long)
	 * 
	 * @param key
	 */
	public static void pop(final String key) {
		timerStack.pop(key);

	}

	public static void pop(final String key, final Long forcedDuration) {
		timerStack.pop(key, forcedDuration);
	}

	/**
	 * Use to push a named element onto the timer stack. This element is
	 * identified by {@code key}. This method does nothing if the timer stack
	 * state is {@code TraceState#OFF}.
	 * 
	 * @see TimerStack#enableAndPush(String)
	 */
	public static void push(final String key) {
		timerStack.push(key);
	}

	/**
	 * Globally set the {@code MetricsLoggerStrategy} to use. This affects all
	 * future traces as well as the trace of the current thread (if one is in
	 * progress). To set the logger for the current thread only, see
	 * {@code TimerStack#setThreadLocalMetricsLogger(MetricsLoggerStrategy)}.
	 * Unlike, the thread-specific setting, this method will work even if
	 * tracing is {@code TraceState#OFF}.
	 * 
	 * The default is {@code BasicMetricsLogger#LOGGER}.
	 * 
	 * @param logger any concrete implementation of
	 *            {@code MetricsLoggerStrategy}.
	 */
	public static void setMetricsLogger(MetricsLoggerStrategy logger) {
		timerStack.setMetricsLogger(logger);
	}

	/**
	 * Effectively enable or disable tracing. It is <b>strongly recommended</b>
	 * to use {@code TraceState#BY_REQUEST} instead of {@code TraceState#OFF},
	 * allowing code to programatically enable tracing for your application when
	 * needed.
	 * 
	 * @param state See {@code TraceState}. Default is
	 *            {@code TraceState#BY_REQUEST}.
	 */
	public static void setState(TraceState state) {
		timerStack.setState(state);
	}

	/**
	 * Set the {@code MetricsLoggerStrategy} for the current thread, only for
	 * the duration of the current stack. That is to say, once the last element
	 * is popped from the stack and the stack is logged, this setting is lost.
	 * See {@code TimerStack#setMetricsLogger(MetricsLoggerStrategy)} for
	 * setting it globally.
	 * 
	 * The default is {@code BasicMetricsLogger#LOGGER}.
	 * 
	 * <B>NOTE:</B> This method does nothing if tracing is
	 * {@code TraceState#OFF}.
	 * 
	 * @see ExceedsDurationMetricsLogger
	 * 
	 * @param logger any concrete implementation of
	 *            {@code MetricsLoggerStrategy}.
	 */
	public static void setThreadLocalMetricsLogger(MetricsLoggerStrategy logger) {
		timerStack.setThreadLocalMetricsLogger(logger);
	}

}
