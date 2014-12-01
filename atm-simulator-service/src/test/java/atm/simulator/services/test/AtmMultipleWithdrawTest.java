package atm.simulator.services.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmStrongBoxService;
import atm.simulator.services.exception.AtmServiceException;

import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"classpath:application-context-service-test.xml"})
public class AtmMultipleWithdrawTest extends AbstractServiceTest {
    
	@Autowired
	private AtmStrongBoxService strongBox;
	
    private TestContextManager testContextManager;
    
    @Before
    public void setUpContext() throws Exception {
    	this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    private long value;
    private BankNote[] cashArray;
    private BankNote[] expected;
    
    public AtmMultipleWithdrawTest(long value, BankNote[] cashArray, BankNote[] expected) {
    	this.value = value;
    	this.cashArray = cashArray;
    	this.expected = expected;
	}

    
    @Test
    public void withdraw() throws AtmServiceException {
        ArrayList<BankNote> bankNotes = Lists.newArrayList(cashArray);

        strongBox.initialise();
        strongBox.addMoney(bankNotes);

        List<BankNote> dispensedMoney = strongBox.withdraw(value);
        Assert.assertTrue(dispensedMoney.containsAll(Lists.newArrayList(expected)));
    }
    
    @Parameters
    public static Collection<Object[]> providerForWithdraw() {
        return Arrays.asList(new Object[][] {
                new Object[] {80,
                        new BankNote[] {bankNote(BankNoteType.TWENTY, 10), bankNote(BankNoteType.FIFTY, 5)},
                        new BankNote[] {bankNote(BankNoteType.TWENTY, 4)}},
                new Object[] {90,
                        new BankNote[] {bankNote(BankNoteType.TWENTY, 10), bankNote(BankNoteType.FIFTY, 5)},
                        new BankNote[] {bankNote(BankNoteType.FIFTY, 1), bankNote(BankNoteType.TWENTY, 2)}},
                new Object[] {100,
                        new BankNote[] {bankNote(BankNoteType.TWENTY, 10), bankNote(BankNoteType.FIFTY, 5)},
                        new BankNote[] {bankNote(BankNoteType.FIFTY, 2)}},
                new Object[] {1000,
                        new BankNote[] {bankNote(BankNoteType.HUNDRED, 5), bankNote(BankNoteType.FIFTY, 50)},
                        new BankNote[] {bankNote(BankNoteType.HUNDRED, 5), bankNote(BankNoteType.FIFTY, 10)}},
                new Object[] {35,
                        new BankNote[] {bankNote(BankNoteType.FIVE, 1), bankNote(BankNoteType.TWENTY, 1), bankNote(BankNoteType.TEN, 35)},
                        new BankNote[] {bankNote(BankNoteType.TWENTY, 1), bankNote(BankNoteType.TEN, 1), bankNote(BankNoteType.FIVE, 1)}},
        });
    }
}
