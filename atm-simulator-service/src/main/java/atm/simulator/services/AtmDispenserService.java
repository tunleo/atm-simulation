package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;
import atm.simulator.services.exception.AtmServiceException;

public interface AtmDispenserService {
	
	public long handleCalculateNoteAmount(long value) throws AtmServiceException;
	
	public long handlePredetermine(long value);
	
	public List<BankNote> handleDispenseBankNotes();
	
	public void setNext(AtmDispenserService dispenser);
	
	public long getNumberOfNotes();
	
	public long addNotes(long quantity);
}
