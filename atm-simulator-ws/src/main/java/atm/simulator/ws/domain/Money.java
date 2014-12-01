package atm.simulator.ws.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Money {
	
	private long value;

	public Money() {
	}

	public Money(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
