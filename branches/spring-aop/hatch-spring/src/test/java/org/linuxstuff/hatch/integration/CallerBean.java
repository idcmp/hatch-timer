package org.linuxstuff.hatch.integration;

public class CallerBean {
	public void setTestBean(IntegrationTestBean testBean) {
		this.testBean = testBean;
	}

	public void startTest() throws InterruptedException {
		testBean.slowMethod();
	}

	private IntegrationTestBean testBean;
}
