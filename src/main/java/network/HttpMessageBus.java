package network;
import java.util.LinkedList;
import java.util.Queue;
import util.GenericEvent;
import util.Worker;

public class HttpMessageBus {
	private static HttpMessageBus instance;
	private static int port;
	private final Queue<GenericEvent<NetworkRequest>> messageQueue; //event_id, port, ip -> search request
	private final Queue<GenericEvent<NetworkRequest>> outgoingQueue; //event_id, port, ip -> search results

	public static int getSpecifiedPort() {
		return port;
	}

	private HttpMessageBus() {
		port = 0;
		messageQueue = new LinkedList<>();
		outgoingQueue = new LinkedList<>();
	}
	public static HttpMessageBus acquire() {
		if(instance == null)
			instance = new HttpMessageBus();
		return instance;
	}
	public static int getPort() {
		return port;
	}

	public static void specifyPort(int _port) {
		if(port == 0)
			port = _port;
		else throw new IllegalStateException("Could not specify port because it has already been set.");
	}
	public synchronized GenericEvent<NetworkRequest> pollRequest(Worker w) {
		if(!messageQueue.isEmpty() && w.getResponsibilities().matchId(messageQueue.peek().getId())) {
			System.out.println("Giving event to worker of type " + w.getClass().getSimpleName());
			return messageQueue.poll();
		}
		return null;
	}
	public synchronized void placeInOutgoingQueue(GenericEvent<NetworkRequest> answer) {
		this.outgoingQueue.add(answer);
	}

	public synchronized void registerRequest(GenericEvent<NetworkRequest> request) {
		this.messageQueue.add(request);
	}
	public synchronized GenericEvent<NetworkRequest> getOutgoigAnswer() {
		if(!this.outgoingQueue.isEmpty())
			return this.outgoingQueue.poll();
		return null;
	}
}
