package atm.simulator.services.test;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.enums.BankNoteType;

public abstract class AbstractServiceTest {
	protected static BankNote bankNote(BankNoteType bankNoteType, long quantity) {
		return new BankNote(bankNoteType, quantity);
    }
}
