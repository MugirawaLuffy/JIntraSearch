package util;

import jdk.jshell.spi.ExecutionControl;
import network.HttpMessageBus;
import network.NetworkRequest;

public abstract class Worker extends Thread{
    @Override
    public void run() {
        while(!PublicState.StopThreads) {
            try {
                getEventAndWork();
                Thread.sleep(10); //give cpu time to take a breath
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Stopping worker thread: " + this.getClass().getSimpleName());
    }

    public abstract IsResponsible getResponsibilities();

    public void getEventAndWork() throws Exception {
        GenericEvent<NetworkRequest> request = HttpMessageBus.acquire().pollRequest(this);
        if(request != null) {
            System.out.println("Thread arbeitet: " + this.getClass().getSimpleName());
            this.work(request);
        }
    }
    public abstract void work(GenericEvent<NetworkRequest> request) throws Exception;
}
