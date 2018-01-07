package pt.upa.broker;

import javax.xml.ws.Endpoint;

import pt.upa.broker.ws.BrokerEndpointManager;

public class BrokerApplication {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + BrokerApplication.class.getName() + " wsURL OR uddiURL wsName wsURL");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		boolean isMain = Boolean.valueOf(args[3]);

		// Create server implementation object, according to options
		System.out.println(isMain);
		BrokerEndpointManager endpoint = null;
			if (args.length == 1) {
				wsURL = args[0];
				endpoint = new BrokerEndpointManager(wsURL);
			} else if (args.length >= 3) {
				uddiURL = args[0];
				wsName = args[1];
				wsURL = args[2];
				if(isMain){ 
					endpoint = new BrokerEndpointManager(uddiURL, wsName, wsURL);
					endpoint.setSecundaryURL("http://localhost:8090/broker-ws/endpoint");
				}
				else { endpoint = new BrokerEndpointManager(args[0], wsName, "http://localhost:8090/broker-ws/endpoint"); }
				endpoint.setVerbose(true);
				endpoint.setMain(isMain);
			}

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}