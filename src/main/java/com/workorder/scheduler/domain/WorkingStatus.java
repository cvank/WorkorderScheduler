package com.workorder.scheduler.domain;

public enum WorkingStatus {

	BUSY(2), IDLE(1), NOT_TAKING_ORDERS(3);

	private int value;

	private WorkingStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
