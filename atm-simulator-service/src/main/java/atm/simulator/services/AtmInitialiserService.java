package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;
import atm.simulator.services.exception.AtmServiceException;

public interface AtmInitialiserService {
	
	public void addBankNotes(List<BankNote> bankNotes) throws AtmServiceException;

	public void clearBankNotes() throws AtmServiceException;
}
