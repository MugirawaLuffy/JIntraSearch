package network;

import util.GenericEvent;
import util.PublicState;

import java.io.IOException;
import java.io.PrintStream;

public class HttpEndpointSender extends Thread{
    public void run() {
        while(!PublicState.StopThreads) {
            try {
                GenericEvent<NetworkRequest> ev = HttpMessageBus.acquire().getOutgoigAnswer();
                if(ev != null) {
                    PrintStream ps = new PrintStream(ev.getPayload().getSocket().getOutputStream());
                    String answer = ev.getValue("answer");
                    if(answer == null) {
                        throw new RuntimeException("Event waiting to be dispatched to client but has no answer prepared. Skip.");
                    }
                    ps.write(answer.getBytes());
                }
                sleep(10);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
