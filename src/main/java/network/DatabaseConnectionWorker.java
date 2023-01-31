package network;
import jdk.jshell.spi.ExecutionControl;
import util.GenericEvent;
import util.IsResponsible;
import util.Worker;

public class DatabaseConnectionWorker extends Worker {

	private IsResponsible iR;
	 
	public DatabaseConnectionWorker(IsResponsible iR) {
		this.iR = iR;
	}
	
	@Override
	public IsResponsible getResponsibilities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void work(GenericEvent<NetworkRequest> request) throws Exception {
		if(request.getId() == EventType.DATABASE_ROLE) {
			//with correct userid, try to get role of user from database
			String userid = request.getValue("userid");
			String role = "";
			request.insertNewAttribute("role", role);
			//put event back into message pump //make sure that the request has been parsed
			if(request.getValue("search") == null) throw new IllegalStateException("Request is ready to be searched, yet no search has been specified");
			HttpMessageBus.acquire().registerRequest(request.respecifyType(EventType.SEARCH_ENGINE_PERFORM));
		} else if(request.getId() == EventType.DATABASE_AUTH) {
			throw new ExecutionControl.NotImplementedException("No functionality added for events of type DATABASE_AUTH");
		}
	}
}
