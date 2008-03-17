package org.linuxstuff.hatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@code DurationBean} is composed of the start and ultimately duration in
 * milliseconds of a particular event (as designated by {@code name}) as well
 * as children events.
 * 
 * @author idcmp
 * 
 */
@SuppressWarnings("serial") public class DurationBean implements Serializable {

	private List<DurationBean> children = new ArrayList<DurationBean>();
	private long duration;

	private final String name;
	private DurationBean parent = null;

	private final long start;

	protected DurationBean(final String key) {
		this.name = key;
		this.start = System.currentTimeMillis();
	}

	protected DurationBean(final String key, long startTimeMillis) {
		this.name = key;
		this.start = startTimeMillis;
	}

	void addChild(DurationBean bean) {
		bean.setParent(this);
		this.children.add(bean);
	}

	protected void end() {
		this.duration = System.currentTimeMillis() - start;
	}

	protected void end(long absoluteDuration) {
		this.duration = absoluteDuration;
	}

	public List<DurationBean> getChildren() {
		return this.children;
	}

	public long getDuration() {
		return this.duration;
	}

	public String getName() {
		return this.name;
	}

	protected DurationBean getParent() {
		return this.parent;
	}

	void setParent(DurationBean bean) {
		this.parent = bean;
	}

	@Override public String toString() {

		return "[DurationBean \"" + name + "\", start=" + this.start + " duration=" + this.duration + "]";

	}
}
