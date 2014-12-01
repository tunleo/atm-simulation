package atm.simulator.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmInitialiserService;
import atm.simulator.services.exception.AtmServiceException;

@Service
public class AtmInitialiserServiceImpl implements AtmInitialiserService{
	
	@Autowired
	private AtmStrongBoxServiceImpl strongBox;
	
	@Override
	public void addBankNotes(List<BankNote> bankNotes) throws AtmServiceException {
		try {
			strongBox.addMoney(bankNotes);
		} catch (Exception e) {
			throw new AtmServiceException(e);
		}
	}

	@Override
	public void clearBankNotes() throws AtmServiceException{
		try {
			strongBox.initialise();
		} catch (Exception e) {
			throw new AtmServiceException(e);
		}
	}
}
