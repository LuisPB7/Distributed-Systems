package ca.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.cert.Certificate;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import ca.ws.CentralAuthority;
import ca.ws.CentralAuthorityImplService;
import ca.ws.Exception_Exception;
// classes generated from WSDL
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CentralAuthorityClient {
	
	static CentralAuthorityImplService service;
	static CentralAuthority port;
	
	static String uddiURL;
	static String name;
	static String entityName;
	
	public CentralAuthorityClient(String UDDIURL, String wsName, String EntityName) throws Exception{
		uddiURL = UDDIURL;
		name = wsName;
		entityName = EntityName;
		lookup();
	}
	
	public void lookup() throws Exception{
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");
		service = new CentralAuthorityImplService();
		port = service.getCentralAuthorityImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		System.out.println(entityName + "hello");

	}

	public String giveCertificate(String entityName) throws Exception {
		return port.giveCertificate(entityName);
	}

}
