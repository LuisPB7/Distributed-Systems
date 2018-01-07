package ca.ws;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.lang.Exception;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import javax.jws.WebService;

import example.ws.handler.SecurityFunctions;

@WebService(endpointInterface = "ca.ws.CentralAuthority")

public class CentralAuthorityImpl implements CentralAuthority {

	public String giveCertificate(String entityName) throws Exception {
		SecurityFunctions func = new SecurityFunctions();
		java.security.cert.Certificate cert = func.readCertificateFile("/home/luispb7/Desktop/A_36-project/keys/"+entityName+"/"+entityName+".cer");
		return func.convertToPem((X509Certificate)cert);
	}

}
