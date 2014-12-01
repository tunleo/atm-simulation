package atm.simulator.domain.enums;

public enum BankNoteType {
	FIVE(5), TEN(10), TWENTY(20), FIFTY(50), HUNDRED(100);

	private final int value;

	BankNoteType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
