package pt.upa.transporter.ws.handler;

import java.io.BufferedInputStream;
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
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import ca.ws.cli.CentralAuthorityClient;
import example.ws.handler.SecurityFunctions;

/**
 * This is the handler server class of the Relay example.
 *
 * #4 The server handler receives data from the client handler (via inbound SOAP
 * message header). #5 The server handler passes data to the server (via message
 * context).
 *
 * *** GO TO server class to see what happens next! ***
 *
 * #8 The server class receives data from the server (via message context). #9
 * The server handler passes data to the client handler (via outbound SOAP
 * message header).
 *
 * *** GO BACK TO client handler to see what happens next! ***
 */

public class TransporterServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY = "my.request.property";
	public static final String RESPONSE_PROPERTY = "my.response.property";

	public static final String REQUEST_HEADER = "securityRequest";
	public static final String REQUEST_NS = "urn:example";

	public static final String RESPONSE_HEADER = "securityResponse";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = TransporterServerHandler.class.getSimpleName();
	public static final String TOKEN = "server-handler";
	
	private CentralAuthorityClient CAClient = null;
	private SecurityFunctions functions = new SecurityFunctions();

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message, VAI ENVIAR SOPA

			// *** #8 ***
			// get token from response context
			String entityAndKeyStoreFile = (String) smc.get(RESPONSE_PROPERTY);
			System.out.printf("%s received '%s'%n", CLASS_NAME, entityAndKeyStoreFile);
			
			String entityName = entityAndKeyStoreFile.split(" ")[0];
			String keyStoreFile = entityAndKeyStoreFile.split(" ")[1];
			
			System.out.println("ENTITY NAME: " + entityName);
            System.out.println("KEY STORE PATH: " + keyStoreFile);
            
			PrivateKey privateKey = null;
			try {
				privateKey = functions.getPrivateKeyFromKeystore(keyStoreFile, "ins3cur3".toCharArray(), entityName, "1nsecure".toCharArray());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			// put token in response SOAP header
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
				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
				SOAPHeaderElement element = sh.addHeaderElement(name);

				// *** #9 ***
				// add header element value
				//String newValue = propertyValue + "," + TOKEN;
				element.addTextNode(entityName + " " + digitalSignature.toString());

				//System.out.printf("%s put token '%s' on response message header%n", CLASS_NAME, TOKEN);

			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// inbound message

			// get token from request SOAP header
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
				Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
				Iterator it = sh.getChildElements(name);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n", REQUEST_HEADER);
					return true;
				}
				SOAPElement element = (SOAPElement) it.next();

				// *** #4 ***
				// get header element value
				String entityName = element.getValue().split(" ",2)[0];
				byte[] signatureReceived = element.getValue().split(" ",2)[1].getBytes();
				System.out.printf("%s got '%s'%n", CLASS_NAME, signatureReceived.toString());
				try {
					CAClient = new CentralAuthorityClient("http://localhost:9090", "CentralAuthority", entityName);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				String publicKeyCertificateString = CAClient.giveCertificate(entityName);
				System.out.println(publicKeyCertificateString);
				java.security.cert.Certificate publicKeyCertificate  = CertificateFactory.getInstance("X509")
						.generateCertificate(new ByteArrayInputStream(publicKeyCertificateString.getBytes()));
				//String certInString = printBase64Binary(BytePublicKeyCertificate);
				//Certificate publicKeyCertificate = new Certificate(certInString);
				//CertificateFactory cf = CertificateFactory.getInstance("X.509");
				//X509Certificate publicKeyCertificate = (X509Certificate)cf.generateCertificate(BytePublicKeyCertificate);
                //System.out.println("GOT CERTIFICATE!!!!!!!!" + publicKeyCertificateString);
                
                
                PublicKey publicKey = publicKeyCertificate.getPublicKey();
                System.out.println("PUBLIC KEY EXISTS: " + publicKey);
                
                
                // IR BUSCAR A PUBLIC KEY!!!!
                boolean check = functions.verifyDigitalSignature(signatureReceived, msg.toString().getBytes(), publicKey);
                if(check){ System.out.println("SIGNATURES MATCH!"); }
                else{ System.out.println("SIGNATURES DON'T MATCH"); }
                
                /*
				// *** #5 ***
				// put token in request context
				String newValue = headerValue + "," + TOKEN;
				System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
				smc.put(REQUEST_PROPERTY, newValue);
				// set property scope to application so that server class can
				// access property
				smc.setScope(REQUEST_PROPERTY, Scope.APPLICATION);
				*/
				return true;

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
