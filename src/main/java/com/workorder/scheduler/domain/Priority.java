package com.workorder.scheduler.domain;

/**
 * Created by chandrashekar.v on 10/24/2016.
 */
public enum Priority {
	CRITICAL(1), HIGH(2), MEDIUM(3), LOW(4);

	int value;

	private Priority(final int val) {
		this.value = val;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
