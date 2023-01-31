package network;

import util.GenericEvent;
import util.IsResponsible;
import util.Worker;

public class LuceneWorker extends Worker {
    IsResponsible iR;

    public LuceneWorker(IsResponsible ir) {
        this.iR = ir;
    }

    @Override
    public IsResponsible getResponsibilities() {
        return this.iR;
    }

    @Override
    public void work(GenericEvent<NetworkRequest> request) throws Exception {

    }
}
