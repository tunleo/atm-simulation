package atm.simulator.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import atm.simulator.domain.enums.BankNoteType;

@XmlRootElement
public class BankNote implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4566274939917754364L;

	
	
	private BankNoteType noteType;
	private long numberOfNotes;

	public BankNote() {}

	public BankNote(BankNoteType noteType, long numberOfNotes) {
		setNoteType(noteType);
		setNumberOfNotes(numberOfNotes);
	}

	public BankNoteType getNoteType() {
		return noteType;
	}

	public BankNote setNoteType(BankNoteType noteType) {
		this.noteType = noteType;
		return this;
	}

	public long getNumberOfNotes() {
		return numberOfNotes;
	}

	public BankNote setNumberOfNotes(long number) {
		this.numberOfNotes = number;
		return this;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BankNote)){
			return false;
		}

		BankNote bankNote = (BankNote) o;

		return numberOfNotes == bankNote.numberOfNotes && noteType == bankNote.noteType;
	}

	@Override
	public int hashCode() {
		int result = noteType.hashCode();
		result = 31 * result + (int) (numberOfNotes ^ (numberOfNotes >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "BankNote{" +
				"noteType=" + noteType +
                ", quantity=" + numberOfNotes +
                '}';
	}
}
