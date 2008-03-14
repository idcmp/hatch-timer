package org.linuxstuff.hatch;

import junit.framework.TestCase;

import org.linuxstuff.hatch.logger.UnitTestingMetricsLogger;

public class TestFailures extends TestCase {

	private UnitTestingMetricsLogger unitTestLogger;

	@Override protected void setUp() throws Exception {
		super.setUp();
		TimerStackUtil.setState(TraceState.ON);

		TimerStackUtil.reset();
		this.unitTestLogger = new UnitTestingMetricsLogger();
		TimerStackUtil.setMetricsLogger(unitTestLogger);
	}

	public void testPopWithoutPush() {
		TimerStackUtil.push("one");
		TimerStackUtil.push("two");
		TimerStackUtil.push("one"); // again
		TimerStackUtil.push("three");
		TimerStackUtil.pop("one");

		assertEquals("There must be one error.", unitTestLogger.errors.size(), 1);
		assertEquals("There must not be any attempt to log the stack.", unitTestLogger.logRequests.size(), 0);
	}

	public void testTooMuchPop() {
		TimerStackUtil.push("one");
		TimerStackUtil.push("two");
		TimerStackUtil.push("one"); // again
		TimerStackUtil.push("three");
		TimerStackUtil.pop("one");
		TimerStackUtil.pop("one");
		TimerStackUtil.pop("one");
		TimerStackUtil.pop("one");
		TimerStackUtil.pop("one");

		assertEquals("There must be one error.", unitTestLogger.errors.size(), 1);
		assertEquals("There must not be any attempt to log the stack.", unitTestLogger.logRequests.size(), 0);
	}


	public void testEmptyPop() {
		TimerStackUtil.pop("huh?");
		assertEquals("pop without ANY pushes must be silently ignored.", unitTestLogger.errors.size(), 0);
		assertEquals("There must not be any attempt to log the stack.", unitTestLogger.logRequests.size(), 0);
	}
}
