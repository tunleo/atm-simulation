package atm.simulator.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import atm.simulator.domain.BankNote;
import atm.simulator.domain.DomainConstant;
import atm.simulator.domain.enums.BankNoteType;
import atm.simulator.services.AtmDispenserService;
import atm.simulator.services.AtmStrongBoxService;
import atm.simulator.services.exception.AtmServiceException;

@Service
public class AtmStrongBoxServiceImpl implements AtmStrongBoxService{

	private Logger logger = LogManager.getLogger(AtmStrongBoxServiceImpl.class);
	
    private AtmDispenserService firstDispenser = null;

    ConcurrentHashMap<BankNoteType, AtmDispenserService> strongBox = new ConcurrentHashMap<>();

    @Override
    public synchronized void initialise() {
    	logger.info("initialising..");
        strongBox.clear();

        AtmDispenserService previousDispenser = null;

        for (int i = BankNoteType.values().length - 1; i >= 0 ; i--) {
        	BankNoteType note = BankNoteType.values()[i];
        	AtmDispenserService dispenser = new AtmDispenserServiceImpl(note);
        	
            strongBox.put(note, dispenser);

            if (previousDispenser == null) {
                firstDispenser = dispenser;
            } else {
                previousDispenser.setNext(dispenser);
            }
            previousDispenser = dispenser;
        }
    }

    @Override
    public synchronized void addMoney(List<BankNote> bankNotes) throws AtmServiceException {
    	validateMoney(bankNotes);
    	for (BankNote bankNote: bankNotes) {
    		AtmDispenserService dispenser = strongBox.get(bankNote.getNoteType());
    		if (dispenser == null){
    			throw new AtmServiceException("No dispenser can be found for: " + bankNote.getNoteType());
    		}
    		dispenser.addNotes(bankNote.getNumberOfNotes());
    	}
	}

    private void validateMoney(List<BankNote> bankNotes) throws AtmServiceException {
    	for (BankNote bankNote: bankNotes) {
    		if (bankNote.getNoteType() == null){
    			throw new AtmServiceException("An invalid note was specified [" + bankNote + "]");
    		}
            if (bankNote.getNumberOfNotes() <= 0){
            	throw new AtmServiceException("Quantity must be bigger than zero [" + bankNote + "]");
            }
            if (bankNote.getNumberOfNotes() > DomainConstant.MAX_SUPPORT_NOTES){
            	throw new AtmServiceException("Quantity must be smaller or equal to " + DomainConstant.MAX_SUPPORT_NOTES + " [" + bankNote + "]");
            }
    	}
    }
    
    @Override
    public synchronized List<BankNote> getMoney() {
        ArrayList<BankNote> bankNotes = new ArrayList<BankNote>();
        for (BankNoteType note : BankNoteType.values()) {
        	AtmDispenserService dispenser = strongBox.get(note);
        	if (dispenser.getNumberOfNotes() > 0){
                bankNotes.add(new BankNote(note, dispenser.getNumberOfNotes()));
        	}
        }
        return bankNotes;
    }

    @Override
    public synchronized List<BankNote> withdraw(long value) throws AtmServiceException {
        validateWithdraw(value);
        long balance = firstDispenser.handleRequestCalculateDispenseNoteAmount(value);
        logger.debug("balance after dispensed : "+balance);
        if (balance > 0) {
            throw new AtmServiceException("The combination of notes available didn't satisfy your request, " +
                                   "please select another amount and try it again.");
        }
        
        return firstDispenser.handleRequestDispenseBankNotes();
    }

    private void validateWithdraw(long value) throws AtmServiceException {
        if (value <= 0){
        	throw new AtmServiceException("Value for withdraw must be bigger than zero.");
        }
        if (firstDispenser == null){
        	throw new AtmServiceException("Dispensers were not initialised correctly.");
        }
        if (value > availableMoney()){
        	throw new AtmServiceException("There is no enough money to full fill the request.");
        }
        if (!isMultiple(value)){
        	throw new AtmServiceException("Value is not multiple or a combination of available notes.");
        }
    }

    private boolean isMultiple(long value) {
        for (BankNoteType note : BankNoteType.values()) {
        	if ((value % note.getValue()) == 0){
        		return true;
        	}
        }
        return false;
    }

    @Override
    public synchronized long getMinimalWithdrawValue() {
    	for (BankNoteType note : BankNoteType.values()) {
    		AtmDispenserService dispenser = strongBox.get(note);
        	if (dispenser.getNumberOfNotes() > 0){
        		return note.getValue();
        	}
    	}
    	return 0;
    }

    @Override
    public synchronized boolean hasEnoughCashFor(long value) {
    	return availableMoney() >= value;
    }

    @Override
    public synchronized long sumTotalMoney() {
    	return availableMoney();
    }

    private long availableMoney() {
        long sumAvailableMoney = 0;
        for (BankNoteType noteType : BankNoteType.values()) {
            sumAvailableMoney += (strongBox.get(noteType).getNumberOfNotes() * noteType.getValue());
        }
        logger.debug("availableMoney : "+sumAvailableMoney);
        return sumAvailableMoney;
    }
}
