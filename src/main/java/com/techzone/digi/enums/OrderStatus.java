package com.techzone.digi.enums;

public enum OrderStatus {

	OPEN(0), READY(1), SENT(2), FINISHED(3);

	private Integer cod;

	private OrderStatus(int cod) {
		this.cod = cod;
	}

	public Integer getCod() {
		if (this.cod == null) {
			return null;
		}
		return this.cod;
	}

	public static OrderStatus toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}

		for (OrderStatus t : OrderStatus.values()) {
			if (cod.equals(t.getCod())) {
				return t;
			}
		}

		throw new IllegalArgumentException("This enum cod is not valid");
	}
}
