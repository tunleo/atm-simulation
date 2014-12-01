package atm.simulator.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmMonitorService;

@Service
public class AtmMonitorServiceImpl implements AtmMonitorService{
	
	@Autowired
	private AtmStrongBoxServiceImpl coffer;
	
	@Override
	public List<BankNote> getAvailableMoney() {
		try {
			return coffer.getMoney();
		} catch (Exception e) {
			//TODO handle
		}
		return null;
	}
}
