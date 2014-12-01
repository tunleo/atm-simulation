package atm.simulator.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmDispenserService;
import atm.simulator.services.exception.AtmServiceException;

public class AtmDispenserServiceImpl implements AtmDispenserService {
	
	private Logger logger = LogManager.getLogger(AtmDispenserServiceImpl.class);
	
    private AtomicLong numberOfNotes = new AtomicLong(0L);
    private BankNoteType noteType;
    private AtmDispenserService next = null;
    private long notesToDispense = 0;

    public AtmDispenserServiceImpl(BankNoteType noteType) {
        this.noteType = noteType;
    }

    @Override
    public long handleRequestCalculateDispenseNoteAmount(long value) throws AtmServiceException {
        long balance = value;
        notesToDispense = 0;
        logger.debug("input value : " + value);
        logger.debug("calculate note type : " + noteType.getValue());
        if (value >= noteType.getValue()) {
            notesToDispense = value / noteType.getValue();
            logger.debug("note to dispense : " + notesToDispense);
            while (notesToDispense > 0) {
                if (notesToDispense > numberOfNotes.get()) {
                    notesToDispense = numberOfNotes.get();
                }
                balance = value - (notesToDispense * noteType.getValue());
                logger.debug("current balance : " + balance);
                if (balance < 0){
                    throw new AtmServiceException("Internal error: balance is negative for " + noteType);
                }
                if (balance == 0){
                    break;
                }

                long predeterminedBalance = predetermine(balance);
                if (predeterminedBalance == 0){
                    break;
                }

                notesToDispense--;
                balance = value - (notesToDispense * noteType.getValue());
            }
        }
        logger.debug("===========calling-next===============");
        return (next != null) ? next.handleRequestCalculateDispenseNoteAmount(balance) : balance;
    }

	@Override
    public long predetermine(long value) {
    	long balance = value;

    	if (value >= noteType.getValue()) {
            long dispenseNotes = (value / noteType.getValue());
            if (dispenseNotes > numberOfNotes.get()) {
                dispenseNotes = numberOfNotes.get();
            }
            balance = value - (dispenseNotes * noteType.getValue());
        }
        return (next != null) ? next.predetermine(balance) : balance;
    }

	@Override
	public List<BankNote> handleRequestDispenseBankNotes() {
        List<BankNote> moneyDispensed = new ArrayList<>();

        if (notesToDispense > 0) {
            moneyDispensed.add(new BankNote(noteType, notesToDispense));
            numberOfNotes.addAndGet(notesToDispense * -1);
            notesToDispense = 0;
        }

        if (next != null)
            moneyDispensed.addAll(next.handleRequestDispenseBankNotes());

        return moneyDispensed;
    }

	@Override
	public void setNext(AtmDispenserService dispenser) {
        next = dispenser;
    }
	
	@Override
    public long getNumberOfNotes() {
        return numberOfNotes.get();
    }

    @Override
	public long addNotes(long quantity) {
        return numberOfNotes.addAndGet(quantity);
    }
	
	
}
