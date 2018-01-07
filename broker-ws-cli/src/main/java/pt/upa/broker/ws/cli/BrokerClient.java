package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
//classes generated by wsimport from WSDL:
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;


/**
 * Client.
 *
 * Adds easier endpoint address configuration and 
 * UDDI lookup capability to the PortType generated by wsimport.
 */
public class BrokerClient implements BrokerPortType {
	/** WS service */
	BrokerService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	BrokerPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS endpoint address */
	private String wsURL = null; // default value is defined inside WSDL

	public String getWsURL() {
		return wsURL;
	}

	/** output option **/
	private boolean verbose = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided web service URL */
	public BrokerClient(String wsURL) throws BrokerClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public BrokerClient(String uddiURL, String wsName) throws BrokerClientException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	/** UDDI lookup */
	private void uddiLookup() throws BrokerClientException {
		try {
			if (verbose)
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose)
				System.out.printf("Looking for '%s'%n", wsName);
			wsURL = uddiNaming.lookup(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!",
					uddiURL);
			throw new BrokerClientException(msg, e);
		}

		if (wsURL == null) {
			String msg = String.format(
					"Service with name %s not found on UDDI at %s", wsName,
					uddiURL);
			throw new BrokerClientException(msg);
		}
	}

	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new BrokerService();
		port = service.getBrokerPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
			/*
			int connectionTimeout = 20000;
            // The connection timeout property has different names in different versions of JAX-WS
            // Set them all to avoid compatibility issues
            final List<String> CONN_TIME_PROPS = new ArrayList<String>();
            CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
            CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
            CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");
            // Set timeout until a connection is established (unit is milliseconds; 0 means infinite)
            for (String propName : CONN_TIME_PROPS)
                requestContext.put(propName, connectionTimeout);
            System.out.printf("Set connection timeout to %d milliseconds%n", connectionTimeout);

            int receiveTimeout = 20000;
            // The receive timeout property has alternative names
            // Again, set them all to avoid compability issues
            final List<String> RECV_TIME_PROPS = new ArrayList<String>();
            RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
            RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
            RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
            // Set timeout until the response is received (unit is milliseconds; 0 means infinite)
            for (String propName : RECV_TIME_PROPS)
                requestContext.put(propName, 20000);
            System.out.printf("Set receive timeout to %d milliseconds%n", receiveTimeout);*/
		}
	}

	// remote invocation methods ----------------------------------------------

	@Override
	public String ping(String name) {
		String result = null;
		try{
			result = port.ping(name);
		}catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
                System.out.println("The cause was a timeout exception: " + cause);
            }
            try {
				uddiLookup();
				createStub();
			} catch (BrokerClientException e) {
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return result;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		String result = null;
		try{
			result = port.requestTransport(origin, destination, price);
		}catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
                System.out.println("The cause was a timeout exception: " + cause);
            }
            try {
				uddiLookup();
				createStub();
			} catch (BrokerClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return result;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		TransportView result = null;
		try{
			result = port.viewTransport(id);
		}catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
                System.out.println("The cause was a timeout exception: " + cause);
            }
            try {
				uddiLookup();
				createStub();
			} catch (BrokerClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return result;
	}

	@Override
	public List<TransportView> listTransports() {
		List<TransportView> result = null;
		try{
			result = port.listTransports();
		}catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
                System.out.println("The cause was a timeout exception: " + cause);
            }
            try {
				uddiLookup();
				createStub();
			} catch (BrokerClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return result;
	}

	@Override
	public void clearTransports() {
		try{
			port.clearTransports();
		}catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
                System.out.println("The cause was a timeout exception: " + cause);
            }
            try {
				uddiLookup();
				createStub();
			} catch (BrokerClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	// main -------------------------------------------------------------------

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + BrokerClient.class.getName()
					+ " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client
		BrokerClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new BrokerClient(wsURL);
		} else if (uddiURL != null) {
			System.out
			.printf("Creating client using UDDI at %s for server with name %s%n",
					uddiURL, wsName);
			client = new BrokerClient(uddiURL, wsName);
		}

		// the following remote invocations are just basic examples
		// the actual tests are made using JUnit

		System.out.println("Invoke ping()...");
		String result = client.ping("client");
		System.out.println(result);
		Thread.sleep(30000);
		System.out.println("Invoke ping()...");
		String result1 = client.ping("timeout!");
		System.out.println(result1);

	}

	@Override
	public void stillAlive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(TransportView updateInfo) {
		// TODO Auto-generated method stub
		
	}
}
