package atm.simulator.ws.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import atm.simulator.domain.BankNote;
import atm.simulator.services.AtmMonitorService;

@Component
@Path("/monitor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AtmMonitorWS {
	
	private Logger logger = LogManager.getLogger(AtmMonitorWS.class);
	
	@Autowired
	private AtmMonitorService monitorService;
	
    @GET
    @Path("/getAvailableMoney")
    public List<BankNote> getAvailableMoney() {
    	logger.info("** Calling getAvailableMoney **");
        try {
            return monitorService.getAvailableMoney();
        }
        catch (Exception e) {
        	e.printStackTrace();
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build());
        }
    }
}
