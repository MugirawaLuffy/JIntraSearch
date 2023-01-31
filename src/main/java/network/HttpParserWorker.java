package network;

import util.GenericEvent;
import util.IsResponsible;
import util.Worker;

public class HttpParserWorker extends Worker {
    IsResponsible ir;
    public HttpParserWorker(IsResponsible iR) {
        this.ir = iR;
    }
    @Override
    public IsResponsible getResponsibilities() {
        return this.ir;
    }
    @Override
    public void work(GenericEvent<NetworkRequest> request) throws Exception {
        System.out.println("Http parser thread found event in queue... processing");
        String message = request.getPayload().getMessage();
        message = message.substring(message.indexOf('?') + 1, message.indexOf(" HTTP/1.1"));
        String[] params = message.split("&");
        for(String s : params) {
            s = s.replace('%', ' ');
            request.insertNewAttribute(s.substring(0, s.indexOf('=')), s.substring(s.indexOf('=')+1, s.length()));
        }
        System.out.println(request.getValue("userid") + ", " + request.getValue("search"));
        //each request needs to set a search phrase and userid
        //after the request has been parsed, put it back into queue for further processing
        System.out.println("Processing finished successfully, putting event back into ready queue for further processing.");
        HttpMessageBus.acquire().registerRequest(request.respecifyType(EventType.DATABASE_ROLE));
    }

}
