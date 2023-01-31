package util;

import network.*;

import java.io.IOException;
import java.util.ArrayList;

public class ThreadingConfiguration {
    private ArrayList<Thread> workers;

    public ThreadingConfiguration(ConfigurationBuilder builder) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Build JIntraSearchThreading configuration...\nSpecs of your configuration:\n");
        workers = new ArrayList<>();
        HttpMessageBus.specifyPort(builder.ports.get(0));
        String ps = "";
        for(int i : builder.ports) {
            workers.add(new HttpEndpointReceiver(i));
            ps += i + ", ";
        }
        ps = ps.substring(0, ps.length()-2);
        //set http listener ports
        buffer.append(builder.ports.size()).append("\tThreads responsible for listening for incoming requests (ports: " + ps + ")\n");
        //set http parser ports
        for(int i = 0; i < builder.httpParserThreads; i++) {
            workers.add(new HttpParserWorker(e -> e == EventType.HTTP_TO_PARSE));
        }
        buffer.append(builder.httpParserThreads).append("\tThreads responsible for parsing incoming requests\n");
        //set database interaction threads
        for(int i = 0; i < builder.databaseConnectionThreads; i++) {
            workers.add(new DatabaseConnectionWorker(e ->
                    e == EventType.DATABASE_ROLE ||
                    e == EventType.DATABASE_AUTH
            ));
        }
        buffer.append(builder.databaseConnectionThreads).append("\tThreads responsible for verifying user access to restricted pages\n");
        //set search engine threads
        for(int i = 0; i < builder.luceneSearchingThreads; i++) {
            workers.add(new LuceneWorker(e ->
                    e == EventType.SEARCH_ENGINE_PERFORM
            ));
        }
        buffer.append(builder.luceneSearchingThreads).append("\tThreads responsible for interfacing the search engine\n");
        // set http sender ports
        for(int i = 0; i < builder.httpSenderThreads; i++) {
            workers.add(new HttpEndpointSender());
        }
        buffer.append(builder.httpSenderThreads).append("\tThreads responsible for sending answers back to clients\n");
        System.out.println(buffer.toString());
    }

    public void startAllThreads() {
        PublicState.StopThreads = false;
        for(Thread t : workers) {
            t.start();
        }
    }

    public void stopAllThreads() {
        PublicState.StopThreads = true;
        for(Thread t : workers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Received interrupt in worker threads");
            }
        }
    }

    public static class ConfigurationBuilder{
        private int httpListenerThreads, httpSenderThreads, httpParserThreads,
            databaseConnectionThreads, luceneSearchingThreads;

        private ArrayList<Integer> ports = new ArrayList<>();

        public ConfigurationBuilder addAllPorts(int... ports) {
            for(int i : ports) {
                this.ports.add(i);
            }
            return this;
        }

        public ConfigurationBuilder setAllThreadAmounts(int number) {
            httpListenerThreads = httpSenderThreads = httpParserThreads = databaseConnectionThreads = luceneSearchingThreads = number;
            return this;
        }
        public ConfigurationBuilder setHttpSenderThreads(int number) {
            httpSenderThreads = number;
            return this;
        }
        public ConfigurationBuilder setHttpParserThreads(int number) {
            httpParserThreads = number;
            return this;
        }
        public ConfigurationBuilder setDatabaseConnectionThreads(int number) {
            databaseConnectionThreads = number;
            return this;
        }
        public ConfigurationBuilder setLuceneSearchingThreads(int number) {
            luceneSearchingThreads = number;
            return this;
        }
        public boolean verifyConfiguration() {
            httpListenerThreads = 1;
            if(httpListenerThreads == 0) {
                System.out.println("Number of http listener threads has to be set to be greater than 0");
            } else if(httpSenderThreads == 0) {
                System.out.println("Number of http parser threads has to be set to be greater than 0");
            } else if(httpParserThreads == 0) {
                System.out.println("Number of http sender threads has to be set to be greater than 0");
            } else if(databaseConnectionThreads == 0) {
                System.out.println("Number of database accessing threads has to be set to be greater than 0");
            } else if(luceneSearchingThreads == 0) {
                System.out.println("Number of lucene threads have to be set to be greater than 0");
            } else {
                return true;
            }
            return false;
        }
        public ThreadingConfiguration build() throws IOException {
            if(this.verifyConfiguration())
                return new ThreadingConfiguration(this);
            throw new RuntimeException("Could not build configuration, failed to verify configuration");
        }


    }
}
