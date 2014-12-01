package atm.simulator.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmWithdrawService;
import atm.simulator.services.exception.AtmServiceException;

@Service
public class AtmWithdrawServiceImpl implements AtmWithdrawService {
	
	@Autowired
	private AtmStrongBoxServiceImpl strongBox;
	
	@Override
	public List<BankNote> withdraw(long value) throws AtmServiceException {
		try {
			return strongBox.withdraw(value);
		} catch (Exception e) {
			throw new AtmServiceException(e);
		}
    }
	
	@Override
	public long minimumValueWithdraw() throws AtmServiceException {
        try {
            return (strongBox.getMinimalWithdrawValue());
        } catch (Exception e) {
        	throw new AtmServiceException(e);
        }
    }

	@Override
    public long maximumValueWithdraw() throws AtmServiceException {
    	try {
    		return strongBox.sumTotalMoney();
    	} catch (Exception e) {
    		throw new AtmServiceException(e);
    	}
	}
}
