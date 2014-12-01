package atm.simulator.ws.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmWithdrawService;
import atm.simulator.ws.domain.Money;

@Component
@Path("/withdraw")
@Produces(MediaType.APPLICATION_JSON)
public class AtmWithdrawWS {
	private Logger logger = LogManager.getLogger(AtmWithdrawWS.class);
	@Autowired
	private AtmWithdrawService withdrawService;
	
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public List<BankNote> withdraw(@QueryParam("value") long value) {
    	logger.info("** calling withdraw with value : "+value);
        try {
        	List<BankNote> withdrawBankNotes = withdrawService.withdraw(value);
        	logger.debug("withdrawBankNotes => "+withdrawBankNotes);
        	
            return withdrawBankNotes;
        }
        catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build());
        }
    }

    @GET
    @Path("/minimumAllow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Money getMinimumWithdrawAllow() {
    	logger.info("** calling getMinimumWithdrawAllow **");
        try {
            return new Money(withdrawService.maximumValueWithdraw());
        }
        catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                                                      .entity(e.getMessage()).build());
        }
    }

    @GET
    @Path("/maximumAllow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Money getMaximumWithdrawAllow() {
    	logger.info("** calling getMaximumWithdrawAllow **");
        try {
            return new Money(withdrawService.maximumValueWithdraw());
        }
        catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build());
        }
    }
}
