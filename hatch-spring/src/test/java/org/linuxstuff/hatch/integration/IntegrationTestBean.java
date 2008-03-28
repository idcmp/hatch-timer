package org.linuxstuff.hatch.integration;

public class IntegrationTestBean {
	private int delay;
	private IntegrationTestBean next;

	public void setDelay(int innerDelay) {
		this.delay = innerDelay;
	}

	public void slowMethod() throws InterruptedException {
		Thread.sleep(delay);
		if (next != null)
			next.slowMethod();
	}

	public void setNext(IntegrationTestBean next) {
		this.next = next;
	}
}
