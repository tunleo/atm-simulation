package atm.simulator.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DomainConstant {
	public static long MAX_SUPPORT_NOTES;
	
	@Value("${MAX_SUPPORT_NOTES}")
	public void setMaxSupportNotes(String maxSupportNotes) {
		DomainConstant.MAX_SUPPORT_NOTES = Long.parseLong(maxSupportNotes);
	}
	
}
