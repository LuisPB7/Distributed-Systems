package pt.upa.transporter.ws.handler;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import ca.ws.cli.CentralAuthorityClient;
import example.ws.handler.SecurityFunctions;

/**
 * This is the handler client class of the Relay example.
 *
 * #2 The client handler receives data from the client (via message context). #3
 * The client handler passes data to the server handler (via outbound SOAP
 * message header).
 *
 * *** GO TO server handler to see what happens next! ***
 *
 * #10 The client handler receives data from the server handler (via inbound
 * SOAP message header). #11 The client handler passes data to the client (via
 * message context).
 *
 * *** GO BACK TO client to see what happens next! ***
 */

public class TransporterClientHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY = "my.request.property";
	public static final String RESPONSE_PROPERTY = "my.response.property";

	public static final String REQUEST_HEADER = "securityRequest";
	public static final String REQUEST_NS = "urn:example";

	public static final String RESPONSE_HEADER = "securityResponse";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = TransporterClientHandler.class.getSimpleName();
	public static final String TOKEN = "client-handler";
	
	private CentralAuthorityClient CAClient = null;

	public boolean handleMessage(SOAPMessageContext smc) {
		SecurityFunctions functions = new SecurityFunctions();
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #2 ***
			// get token from request context
			String entityAndKeyStoreFile = (String) smc.get(REQUEST_PROPERTY);
			System.out.printf("%s received '%s'%n", CLASS_NAME, entityAndKeyStoreFile);
			String entityName = entityAndKeyStoreFile.split(" ")[0];
			String keyStoreFile = entityAndKeyStoreFile.split(" ")[1];
			try {
				CAClient = new CentralAuthorityClient("http://localhost:9090", "CentralAuthority", entityName);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("ENTITY NAME: " + entityName);
            System.out.println("KEY STORE PATH: " + keyStoreFile);
            
            PrivateKey privateKey = null;
			try {
				privateKey = functions.getPrivateKeyFromKeystore(keyStoreFile, "ins3cur3".toCharArray(), entityName, "1nsecure".toCharArray());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("PRIVATE KEY = " + privateKey);
			// put token in request SOAP header
			try {
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				
				byte[] digitalSignature = functions.makeDigitalSignature(msg.toString().getBytes(), privateKey);

				// add header
				SOAPHeader sh = se.getHeader();
				if (sh == null)
					sh = se.addHeader();

				// add header element (name, namespace prefix, namespace)
				Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
				SOAPHeaderElement element = sh.addHeaderElement(name);

				// *** #3 ***
				// add header element value
				//String newValue = propertyValue + "," + TOKEN;
				element.addTextNode(entityName + " " + digitalSignature.toString()); //toString!
				
				System.out.printf("%s put token '%s' on request message header%n", CLASS_NAME, digitalSignature.toString());

			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// inbound message

			// get token from response SOAP header
			try {
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}

				// get first header element
				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
				Iterator it = sh.getChildElements(name);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n", RESPONSE_HEADER);
					return true;
				}
				SOAPElement element = (SOAPElement) it.next();
				
				byte[] signatureReceived = element.getValue().split(" ")[1].getBytes();
				String entityName = element.getValue().split(" ")[0];
				try {
					CAClient = new CentralAuthorityClient("http://localhost:9090", "CentralAuthority", entityName);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				String publicKeyCertificateString = CAClient.giveCertificate(entityName);
				java.security.cert.Certificate publicKeyCertificate  = CertificateFactory.getInstance("X509")
						.generateCertificate(new ByteArrayInputStream(publicKeyCertificateString.getBytes()));
                //System.out.println("GOT CERTIFICATE!!!!!!!!" + publicKeyCertificateString);
                
                PublicKey publicKey = publicKeyCertificate.getPublicKey();
                
                boolean check = functions.verifyDigitalSignature(signatureReceived, msg.toString().getBytes(), publicKey);
				// *** #10 ***
				// get header element value
				//String headerValue = element.getValue();
				//System.out.printf("%s got '%s'%n", CLASS_NAME, headerValue);

				// *** #11 ***
				// put token in response context
				//String newValue = headerValue + "," + TOKEN;
				//System.out.printf("%s put token '%s' on response context%n", CLASS_NAME, TOKEN);
				smc.put(RESPONSE_PROPERTY, signatureReceived);
				// set property scope to application so that client class can
				// access property
				smc.setScope(RESPONSE_PROPERTY, Scope.APPLICATION);
				
				return check;

			} catch (SOAPException e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext messageContext) {
	}

}
