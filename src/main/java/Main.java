import network.HttpMessageBus;
import util.ThreadingConfiguration;

import java.io.IOException;
import java.util.Scanner;
public class Main {
	public static void main(String[] args) throws InterruptedException {
		try {
			HttpMessageBus.acquire();
			ThreadingConfiguration runtimeConfiguration = new ThreadingConfiguration.ConfigurationBuilder()
					.setAllThreadAmounts(5)
					.setHttpParserThreads(1)
					.addAllPorts(80, 1313, 4500, 50000)
					.setLuceneSearchingThreads(15)
					.build();
			runtimeConfiguration.startAllThreads();
			/*
Build JIntraSearchThreading configuration...
Specs of your configuration:
4	Threads responsible for listening for incoming requests (ports: 80, 1313, 4500, 50000)
1	Threads responsible for parsing incoming requests
5	Threads responsible for verifying user access to restricted pages
15	Threads responsible for interfacing the search engine
5	Threads responsible for sending answers back to clients
Request received on port 80, putting into message bus for further processing
Process finished with exit code 130
			* */

			Scanner input = new Scanner(System.in);
			while(true) {
				String line = input.nextLine();
				if("stop".equalsIgnoreCase(line)) break;
				Thread.sleep(100);
			}
			Thread.sleep(1000);
			runtimeConfiguration.stopAllThreads();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
