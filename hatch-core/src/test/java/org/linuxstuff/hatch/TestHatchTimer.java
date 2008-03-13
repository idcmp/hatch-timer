package org.linuxstuff.hatch;

import junit.framework.TestCase;

import org.linuxstuff.hatch.logger.NullMetricsLogger;
import org.linuxstuff.hatch.logger.UnitTestingMetricsLogger;

public class TestHatchTimer extends TestCase {

	private UnitTestingMetricsLogger unitTestLogger;

	@Override protected void setUp() throws Exception {
		super.setUp();
		TimerStackUtil.setState(TraceState.ON);

		TimerStackUtil.reset();
		this.unitTestLogger = new UnitTestingMetricsLogger();
		TimerStackUtil.setMetricsLogger(unitTestLogger);
	}

	public void testSimple() {

		TimerStackUtil.push("work");
		TimerStackUtil.pop("work");
		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);
	}

	public void testSimpleFixed() {
		TimerStackUtil.push("work");
		TimerStackUtil.pop("work", 1000l);

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);
		assertEquals("This stack must have a duration of 1000ms", unitTestLogger.logRequests.keySet().iterator().next()
				.getDuration(), 1000);

	}

	public void testStateOff() {
		TimerStackUtil.setState(TraceState.OFF);
		TimerStackUtil.push("work");
		TimerStackUtil.pop("work");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must not be any stacks logged.", unitTestLogger.logRequests.size(), 0);

	}

	public void testStateByRequestWithoutRequest() {
		TimerStackUtil.setState(TraceState.BY_REQUEST);
		TimerStackUtil.push("work");
		TimerStackUtil.pop("work");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must not be any stacks logged.", unitTestLogger.logRequests.size(), 0);

	}

	public void testStateByRequestWithRequest() {
		TimerStackUtil.setState(TraceState.BY_REQUEST);
		TimerStackUtil.enableAndPush("work");
		TimerStackUtil.pop("work");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);

	}

	public void testIgnoredByRequest() {

		TimerStackUtil.enableAndPush("work");
		TimerStackUtil.setState(TraceState.BY_REQUEST);
		TimerStackUtil.pop("work");
		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);
	}

	public void testStateByRequestWithDelayedRequest() {
		TimerStackUtil.setState(TraceState.BY_REQUEST);
		TimerStackUtil.push("work-before-request");
		TimerStackUtil.enableAndPush("work");
		TimerStackUtil.pop("work");
		TimerStackUtil.pop("work-before-request");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);

	}

	public void testLateOff() {
		TimerStackUtil.enableAndPush("work");
		TimerStackUtil.setState(TraceState.OFF);
		TimerStackUtil.pop("work");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must not be any attempts log the stack.", unitTestLogger.logRequests.size(), 0);

	}

	public void testThreadSpecificLogger() {

		TimerStackUtil.setMetricsLogger(NullMetricsLogger.LOGGER);
		TimerStackUtil.setThreadLocalMetricsLogger(unitTestLogger);
		TimerStackUtil.push("work");
		TimerStackUtil.pop("work");
		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must only be one attempt to log the stack.", unitTestLogger.logRequests.size(), 1);
	}

	public void testGlobalLogger() {

		TimerStackUtil.setMetricsLogger(NullMetricsLogger.LOGGER);
		TimerStackUtil.push("work");
		TimerStackUtil.pop("work");
		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("There must not be any attempts to log the stack.", unitTestLogger.logRequests.size(), 0);
	}

	public void testSmallStack() {
		TimerStackUtil.push("web-request");
		TimerStackUtil.push("one");
		TimerStackUtil.push("two");
		TimerStackUtil.push("three");
		TimerStackUtil.push("four");
		TimerStackUtil.push("five");
		TimerStackUtil.push("six");

		TimerStackUtil.pop("six");
		TimerStackUtil.pop("five");
		TimerStackUtil.pop("four");
		TimerStackUtil.pop("three");
		TimerStackUtil.pop("two");
		TimerStackUtil.pop("one");
		TimerStackUtil.pop("web-request");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("Must have only one attempt to log stack: ", unitTestLogger.logRequests.size(), 1);
		assertEquals("Dump must have seven lines!", unitTestLogger.linesInLasttTrace(), 7);

	}

	public void testLoud2() {
		TimerStackUtil.push("one");
		TimerStackUtil.push("two");
		TimerStackUtil.pop("two", 5l);
		TimerStackUtil.push("two");
		TimerStackUtil.pop("two", 5l);
		TimerStackUtil.push("two");
		TimerStackUtil.push("three");
		TimerStackUtil.pop("three", 3l);
		TimerStackUtil.pop("two", 8l);
		TimerStackUtil.push("two");
		TimerStackUtil.pop("two", 5l);
		TimerStackUtil.pop("one", 25l);

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("Must have only one attempt to log stack: ", unitTestLogger.logRequests.size(), 1);
		assertEquals("Dump must have six lines!", unitTestLogger.linesInLasttTrace(), 6);
		System.out.println(unitTestLogger.getLastDump());
		

	}

	
	
	/**
	 * [XXms] outer [1ms] inner [2ms] inner [3ms] inner etc..
	 */
	public void testSimpleLoop() {

		TimerStackUtil.push("outer");

		for (long i = 1; i != 10; i++) {
			TimerStackUtil.push("inner");
			TimerStackUtil.pop("inner", i);
		}

		TimerStackUtil.pop("outer");

		assertEquals("There must not be any errors.", unitTestLogger.errors.size(), 0);
		assertEquals("Must have only one attempt to log stack: ", unitTestLogger.logRequests.size(), 1);
		assertEquals("Dump must have 10 lines!", unitTestLogger.linesInLasttTrace(), 10);
	}
}
