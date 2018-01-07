package ca.ws;

import java.security.cert.Certificate;

import javax.jws.WebService;

@WebService
public interface CentralAuthority {

	String giveCertificate(String entityName) throws java.lang.Exception;

}
