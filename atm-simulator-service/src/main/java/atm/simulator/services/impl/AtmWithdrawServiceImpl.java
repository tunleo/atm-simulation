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
	private AtmStrongBoxServiceImpl coffer;
	
	@Override
	public List<BankNote> withdraw(long value) {
		try {
			return coffer.withdraw(value);
		} catch (AtmServiceException e) {
			//TODO handle
		} catch (Exception e) {
			//TODO handle
		}
		return null;
    }
	
	@Override
	public long minimumValueWithdraw() {
        try {
            return (coffer.getMinimalWithdrawValue());
        } catch (Exception e) {
        	//TODO handle
        }
        return 0;
    }

	@Override
    public long maximumValueWithdraw() {
    	try {
    		return coffer.sumTotalMoney();
    	} catch (Exception e) {
    		//TODO handle
    	}
    	return 0;
	}
}
