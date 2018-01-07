package example.crypto;

// provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.MessageDigest;

/**
 * Generate a digest using the SHA-1 algorithm.
 */
public class Digest {

	public static void main(String[] args) throws Exception {

		// check args and get plaintext
		if (args.length != 1) {
			System.err.println("args: (text)");
			return;
		}
		final String plainText = args[0];
		final byte[] plainBytes = plainText.getBytes();

		System.out.println("Text:");
		System.out.println(plainText);
		System.out.println("Bytes:");
		System.out.println(printHexBinary(plainBytes));

		// get a message digest object using the specified algorithm
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		System.out.println(messageDigest.getProvider().getInfo());

		System.out.println("Computing digest ...");
		messageDigest.update(plainBytes);
		byte[] digest = messageDigest.digest();

		System.out.println("Digest:");
		System.out.println(printHexBinary(digest));
	}

}
