package atm.simulator.services;

import java.util.List;

import atm.simulator.domain.BankNote;
import atm.simulator.services.exception.AtmServiceException;


public interface AtmMonitorService {
	
	public List<BankNote> getAvailableMoney() throws AtmServiceException ;
}
