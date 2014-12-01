package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;

public interface AtmWithdrawService {
	
	public List<BankNote> withdraw(long value);
	
	public long minimumValueWithdraw();
	
	public long maximumValueWithdraw();
}
