package org.linuxstuff.hatch;

import junit.framework.TestCase;

import org.linuxstuff.hatch.logger.UnitTestingMetricsLogger;

public class TestFailures extends TestCase {

	private UnitTestingMetricsLogger unitTestLogger;

	@Override protected void setUp() throws Exception {
		super.setUp();
		TimerStackUtil.setState(TraceState.ON);

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

	public void testEmtpyPop() {
		TimerStackUtil.pop("huh?");
		assertEquals("There must be one error.", unitTestLogger.errors.size(), 1);
		assertEquals("There must not be any attempt to log the stack.", unitTestLogger.logRequests.size(), 0);
	}
}
