package atm.simulator.services.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.DomainConstant;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmInitialiserService;
import atm.simulator.services.AtmStrongBoxService;
import atm.simulator.services.exception.AtmServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-service-test.xml"})
public class AtmInitialiserTest extends AbstractServiceTest {
	
	private Logger logger = LogManager.getLogger(AtmInitialiserTest.class);
	
	@Autowired
	private AtmInitialiserService atmInitService;
	
	@Autowired
	private AtmStrongBoxService strongBox;
	
	//should<Expecting>When<Criteria>On<MehtodName>
	@Test
	public void init_shouldSuccessWhenAddEmptyBankNote() throws AtmServiceException{
		atmInitService.addBankNotes(new ArrayList<BankNote>());
	}
	
	@Test
	public void init_shouldZeroCashForAllNotes() {
		strongBox.initialise();
		Assert.assertTrue(strongBox.getMoney().isEmpty());
		Assert.assertEquals(0,strongBox.sumTotalMoney());
	}
	
	@Test
	public void add_shouldAddMoneyToCashBox_whenBoxIsEmpty() throws AtmServiceException {
		List<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TEN, 20), bankNote(BankNoteType.FIFTY, 10), bankNote(BankNoteType.HUNDRED, 10));

		strongBox.initialise();
		strongBox.addMoney(bankNotes);

		Assert.assertTrue(strongBox.getMoney().containsAll(bankNotes));
		Assert.assertEquals(strongBox.sumTotalMoney(),((10 * 20) + (50 * 10) + (100 * 10)));
	}
	
	@Test
    public void add_shouldAddMoneyToCashBox_whenBoxIsNotEmpty() throws AtmServiceException {
		List<BankNote> bankNotesLotOne = Lists.newArrayList(bankNote(BankNoteType.TEN, 5), bankNote(BankNoteType.TWENTY, 5), bankNote(BankNoteType.FIFTY, 10));
		List<BankNote> bankNotesLotTwo = Lists.newArrayList(bankNote(BankNoteType.FIVE, 20), bankNote(BankNoteType.HUNDRED, 10));

		strongBox.initialise();
		strongBox.addMoney(bankNotesLotOne);
		strongBox.addMoney(bankNotesLotTwo);
        Assert.assertTrue(strongBox.getMoney().containsAll(Lists.newArrayList(Iterables.concat(bankNotesLotOne, bankNotesLotTwo))));
        Assert.assertEquals(strongBox.sumTotalMoney(), (10 * 5) + (20 * 5) + (50 * 10) + (5 * 20) + (100 * 10));
    }
	
	@Test(expected = AtmServiceException.class)
	public void add_shouldThrowException_whenQuantityIsNegative() throws AtmServiceException {
		ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 20), bankNote(BankNoteType.FIFTY, -1));

		strongBox.initialise();
		strongBox.addMoney(bankNotes);
	}
	
    @Test(expected = AtmServiceException.class)
    public void add_shouldThrowException_whenQuantityIsZero() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 0), bankNote(BankNoteType.FIFTY, 10));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);
    }

    @Test(expected = AtmServiceException.class)
    public void add_shouldThrowException_whenQuantityIsBiggerThanMaxAllowed() throws AtmServiceException {
    	ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, DomainConstant.MAX_SUPPORT_NOTES + 1), bankNote(BankNoteType.FIFTY, 10));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);
    }
    
	@Test
    public void getMinimalWithdrawValue_shouldBeZero_whenThereIsNoMoney() {
    	strongBox.initialise();
        Assert.assertTrue(strongBox.sumTotalMoney()==0);
        Assert.assertTrue(strongBox.getMinimalWithdrawValue()==0);
    }
    
    @Test
    public void getMinimalWithdrawValue_shouldBeUpdated_afterEachLastWithdraw() throws AtmServiceException {
    	long bankNoteTwentyQty = 4;
    	long bankNoteFiftyQty = 1;
        ArrayList<BankNote> bankNotes 
        			= Lists.newArrayList(bankNote(BankNoteType.TWENTY, bankNoteTwentyQty), bankNote(BankNoteType.FIFTY, bankNoteFiftyQty));

        strongBox.initialise();
        Assert.assertTrue(strongBox.sumTotalMoney()==0);
        strongBox.addMoney(bankNotes);
        Assert.assertEquals(strongBox.sumTotalMoney(), (BankNoteType.TWENTY.getValue() * bankNoteTwentyQty) + (BankNoteType.FIFTY.getValue() * bankNoteFiftyQty));
        Assert.assertEquals(20,strongBox.getMinimalWithdrawValue());

        List<BankNote> withdrawBankNotes = strongBox.withdraw(80);
        logger.debug("calculated withdrawBankNotes : "+withdrawBankNotes);
        Assert.assertEquals(50,strongBox.getMinimalWithdrawValue());
    }
    
    @Test
    public void hasEnoughCashFor_shouldReturnTrue_whenMoneyInTheBoxIsEnough() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 10), bankNote(BankNoteType.FIFTY, 20));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);

        Assert.assertTrue(strongBox.hasEnoughCash(400));
    }
    
    @Test
    public void hasEnoughCashFor_shouldReturnFalse_whenMoneyInTheBoxIsNotEnough() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(bankNote(BankNoteType.TWENTY, 1), bankNote(BankNoteType.FIFTY, 1));

        strongBox.initialise();
        strongBox.addMoney(bankNotes);

        Assert.assertFalse(strongBox.hasEnoughCash(200));
    }
}
