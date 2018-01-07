package example.crypto;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Set;

/**
 * List all available encryption and digest algorithms
 */
public class ListAlgos {

	public static void main(String[] args) throws Exception {

		System.out.println("List of all available encryption and digest algorithms:");

		Provider[] provList = Security.getProviders();
		for (Provider p : provList) {
			Set<Service> servList = p.getServices();
			for (Service s : servList) {
				System.out.println(s.getAlgorithm());
			}
		}
	}
}
