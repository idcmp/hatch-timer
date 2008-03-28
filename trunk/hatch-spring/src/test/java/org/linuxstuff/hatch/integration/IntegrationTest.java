package org.linuxstuff.hatch.integration;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IntegrationTest {
	@Test
	public void demonstrateAspect() throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"integration-test-context.xml");

		CallerBean integrationTestBean = (CallerBean) context
				.getBean("integrationBean");

		integrationTestBean.startTest();
	}
}
