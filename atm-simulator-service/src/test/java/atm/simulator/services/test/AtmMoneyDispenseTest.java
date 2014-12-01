package atm.simulator.services.test;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import atm.simulator.domain.BankNote;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmDispenserService;
import atm.simulator.services.exception.AtmServiceException;
import atm.simulator.services.impl.AtmDispenserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-service-test.xml"})
public class AtmMoneyDispenseTest {
	
	private Logger logger = LogManager.getLogger(AtmMoneyDispenseTest.class);
	
    @Test
    public void calculate_shouldReturnFullBalance_whenItCanNotHandlefullTheRequest() throws AtmServiceException {
    	logger.info("calculate_shouldReturnFullBalance_whenItCanNotHandlefullTheRequest");
        AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.FIFTY);
        dispenser.addNotes(4);
        Assert.assertEquals(4, dispenser.getNumberOfNotes());

        long balance = dispenser.handleCalculateNoteAmount(120);

        Assert.assertEquals(120,balance);
    }
    
    @Test
    public void calculate_willNotUseNotes_whenValueIsSmallerThanNote() throws AtmServiceException {
    	AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.FIFTY);
    	dispenser.addNotes(2);
    	
    	long balance = dispenser.handleCalculateNoteAmount(40);

    	Assert.assertEquals(40,balance);
    }
    
    @Test
	public void calculate_shouldCallTheNextHandleInChain() throws AtmServiceException {
    	AtmDispenserService next = mock(AtmDispenserServiceImpl.class);
    	AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.TWENTY);

    	dispenser.setNext(next);
    	dispenser.addNotes(2);
    	dispenser.handleCalculateNoteAmount(30);

    	verify(next).handleCalculateNoteAmount(10);
    }
    
	@Test
	public void dispense_shouldUpdateTheNumberOfNotes() throws AtmServiceException {
		AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.FIFTY);
		dispenser.addNotes(3);
		Assert.assertEquals(3,dispenser.getNumberOfNotes());

		dispenser.handleCalculateNoteAmount(100);
		List<BankNote> moneyDispensed = dispenser.handleDispenseBankNotes();

		Assert.assertEquals(1,dispenser.getNumberOfNotes());
		Assert.assertTrue(moneyDispensed.contains(new BankNote(BankNoteType.FIFTY, 2)));
	}
	
	@Test
	public void dispense_shouldNotChangeTheNumberOfNotes_whenItCanNotFullfilTheRequest() throws AtmServiceException {
		AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.FIFTY);
		dispenser.addNotes(3);
		Assert.assertEquals(3, dispenser.getNumberOfNotes());

		dispenser.handleCalculateNoteAmount(120);
		List<BankNote> moneyDispensed = dispenser.handleDispenseBankNotes();

		Assert.assertEquals(3, dispenser.getNumberOfNotes());
		Assert.assertTrue(moneyDispensed.isEmpty());
    }
	
	@Test
	public void dispense_shouldCallTheNextInChain() throws AtmServiceException {
		AtmDispenserService next = mock(AtmDispenserServiceImpl.class);
		AtmDispenserService dispenser = new AtmDispenserServiceImpl(BankNoteType.FIFTY);

		dispenser.setNext(next);
		dispenser.addNotes(3);
		Assert.assertEquals(3,dispenser.getNumberOfNotes());

		dispenser.handleCalculateNoteAmount(120);
		dispenser.handleDispenseBankNotes();

		verify(next).handleDispenseBankNotes();
    }
}
