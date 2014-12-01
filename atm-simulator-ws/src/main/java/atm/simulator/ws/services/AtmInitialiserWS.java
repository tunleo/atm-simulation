package atm.simulator.ws.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmInitialiserService;
import atm.simulator.services.exception.AtmServiceException;

@Component
@Path("/init")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AtmInitialiserWS {
	
	@Autowired
	private AtmInitialiserService initialiserService;
	
	@POST
    @Path("/addMoney")
	public Response addMoney(List<BankNote> bankNotes) {
		try {
			initialiserService.addBankNotes(bankNotes);
        }
        catch (AtmServiceException e) {
            throw new WebApplicationException(
                    Response.status(Status.INTERNAL_SERVER_ERROR)
                            .entity(e.getMessage()).build());
        }
        catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build());
        }
        return Response.status(Status.OK).build();
	}
	
	@POST
    @Path("/resetAtm")
    public Response resetAtm() {
        try {
        	initialiserService.clearBankNotes();
        }
        catch (Exception e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                                                      .entity(e.getMessage()).build());
        }
        return Response.status(Status.OK).build();
    }
}
