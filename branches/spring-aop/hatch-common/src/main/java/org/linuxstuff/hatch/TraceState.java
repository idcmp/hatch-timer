package org.linuxstuff.hatch;


public enum TraceState {
	/**
	 * ON: All calls to push/pop are recorded. When the last pop is done, the
	 * stack is logged and all thread-local variables are reset.
	 */
	ON,

	/**
	 * OFF: Aggressively shut off any tracing. Individual threads <b>CANNOT</b>
	 * override tracing if it is turned off.
	 */
	OFF,

	/**
	 * BY_REQUEST: Effectively turn tracing off unless an individual thread
	 * requests to be traced {@see TimerStack#enableAndPush(String)}. Until
	 * tracing is enabled, all requests to push elements onto the stack are
	 * ignored. <b>This is the default, and unless you're having performance
	 * problems, you should use it instead of {@code TraceState#OFF}.</b>
	 */
	BY_REQUEST
}
