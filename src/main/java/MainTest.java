import network.*;
import util.GenericEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainTest {
    public static void main(String[] args) throws IOException {
        HttpEndpointReceiver receiver 
            = new HttpEndpointReceiver(80);
        receiver.start();
        HttpParserWorker worker = new HttpParserWorker(e -> 
                e == EventType.HTTP_TO_PARSE
        );
        worker.start();
    }
}
/*Erzeugt Ausgabe:

Request received on port 80, putting into message bus for further processing
Giving event to worker of type HttpParserWorker
Thread arbeitet: HttpParserWorker
Http parser thread found event in queue... processing
1, nils ist cool
Processing finished successfully, putting event back into ready queue for further processing.
* */
