package atm.simulator.services.exception;

public class AtmServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8018417210498387345L;
	
	public AtmServiceException() {
        super();
    }
	
    public AtmServiceException(String message) {
        super(message);
    }
    
    public AtmServiceException(Exception e){
    	super(e);
    }

}
