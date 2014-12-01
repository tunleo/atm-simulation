package atm.simulator.services.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmStrongBoxService;
import atm.simulator.services.exception.AtmServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-service-test.xml"})
public class AtmWithdrawTest extends AbstractServiceTest {

	@Autowired
	private AtmStrongBoxService strongBox;
	
    @Test(expected = AtmServiceException.class)
    public void withdraw_shouldThrowException_whenValueIsZero() throws AtmServiceException {
    	strongBox.withdraw(0);
    }
    
    @Test(expected = AtmServiceException.class)
    public void withdraw_shouldThrowException_whenThereIsNoEnoughMoney() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 4), bankNote(BankNoteType.FIFTY, 1));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);
        Assert.assertEquals(130, strongBox.sumTotalMoney());

        strongBox.withdraw(150);
    }
    
    @Test(expected = AtmServiceException.class)
    public void withdraw_shouldThrowException_whenValueIsNotMultipleOfAvailableNotes() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.FIVE, 20), bankNote(BankNoteType.FIFTY, 20));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);

        strongBox.withdraw(33);
    }

    @Test
    public void withdraw_shouldUpdateAmountOfNotes() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 10), bankNote(BankNoteType.FIFTY, 20));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);
        Assert.assertEquals(10 * 20 + 50 * 20,strongBox.sumTotalMoney());

        strongBox.withdraw(90);
        Assert.assertEquals(10 * 20 + 50 * 20 - 90,strongBox.sumTotalMoney());
    }

}
