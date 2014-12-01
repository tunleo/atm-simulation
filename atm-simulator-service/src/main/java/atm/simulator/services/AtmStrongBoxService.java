package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;
import atm.simulator.services.exception.AtmServiceException;

public interface AtmStrongBoxService {

	public void initialise();
	
	public void addMoney(List<BankNote> money) throws AtmServiceException;
	
	public List<BankNote> getBankNotes();
	
	public List<BankNote> withdraw(long value) throws AtmServiceException;
	
	public long getMinimalWithdrawValue();

    public boolean hasEnoughCash(long value);

    public long sumTotalMoney();
}
