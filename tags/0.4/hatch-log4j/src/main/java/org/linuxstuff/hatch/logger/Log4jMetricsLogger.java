package org.linuxstuff.hatch.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.linuxstuff.hatch.DurationBean;
import org.linuxstuff.hatch.logger.MetricsLoggerStrategy;

public class Log4jMetricsLogger extends BasicMetricsLogger implements MetricsLoggerStrategy {

	Logger log = Logger.getLogger("org.linuxstuff.hatch.MetricsLogger");

	/**
	 * Dump the error and a stack trace. I totally cheat here by just throwing
	 * an exception.
	 */
	public void error(String message) {
		log.error(message, new Exception("Stack trace follows."));
	}

	public void logMetrics(DurationBean durationBean) {

		if (!log.isInfoEnabled()) {
			return;
		}

		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

		logMetric(new PrintStream(byteArrayStream), "", durationBean);

		log.info(byteArrayStream.toString());

	}

}
