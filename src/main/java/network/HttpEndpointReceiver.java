package network;

import util.GenericEvent;
import util.PublicState;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpEndpointReceiver extends Thread{
    private ServerSocket serverSocket;
    private int port;
    public HttpEndpointReceiver(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    @Override
    public void run() {
        while(!PublicState.StopThreads) {
            try {
                Socket s = serverSocket.accept();
                System.out.println("Request received on port " + this.port + ", putting into message bus for further processing");
                NetworkRequest request = new NetworkRequest(s);
                HttpMessageBus.acquire().registerRequest(new GenericEvent<>(EventType.HTTP_TO_PARSE, request));
            } catch( InterruptedIOException ir) {
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Stopping listener thread.");
    }
}
