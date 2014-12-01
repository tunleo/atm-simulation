package atm.simulator.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmMonitorService;
import atm.simulator.services.exception.AtmServiceException;

@Service
public class AtmMonitorServiceImpl implements AtmMonitorService{
	
	@Autowired
	private AtmStrongBoxServiceImpl strongBox;
	
	@Override
	public List<BankNote> getAvailableMoney() throws AtmServiceException {
		try {
			return strongBox.getBankNotes();
		} catch (Exception e) {
			throw new AtmServiceException(e);
		}
	}
}
