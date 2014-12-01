package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;


public interface AtmMonitorService {
	
	public List<BankNote> getAvailableMoney();
}
