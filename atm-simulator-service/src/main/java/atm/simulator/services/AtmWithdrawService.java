package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;
import atm.simulator.services.exception.AtmServiceException;

public interface AtmWithdrawService {
	
	public List<BankNote> withdraw(long value) throws AtmServiceException ;
	
	public long minimumValueWithdraw() throws AtmServiceException ;
	
	public long maximumValueWithdraw() throws AtmServiceException ;
}
