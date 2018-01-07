package example.crypto;

// provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.SecureRandom;

/**
 * Generate secure random numbers.
 */
public class SecureRandomNumber {

	public static void main(String[] args) throws Exception {

		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		System.out.println(random.getProvider().getInfo());

		System.out.println("Generating random byte array ...");

		final byte array[] = new byte[16];
		random.nextBytes(array);

		System.out.println("Results:");
		System.out.println(printHexBinary(array));
	}

}
