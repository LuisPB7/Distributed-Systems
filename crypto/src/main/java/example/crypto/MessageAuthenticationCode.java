package example.crypto;

//provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Generate a Message Authentication Code using the Mac object provided by Java
 */
public class MessageAuthenticationCode {

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

		// generate AES secret key
		SecretKey key = generate();

		// make MAC
		System.out.println("Signing ...");
		byte[] cipherDigest = makeMAC(plainBytes, key);
		System.out.println("CipherDigest:");
		System.out.println(printHexBinary(cipherDigest));

		// verify the MAC
		System.out.println("Verifying ...");
		boolean result = verifyMAC(cipherDigest, plainBytes, key);
		System.out.println("MAC is " + (result ? "right" : "wrong"));

		// data modification ...
		plainBytes[3] = 12;
		System.out.println("Tampered bytes:");
		System.out.println(printHexBinary(plainBytes));

		// verify the MAC
		System.out.println("Verifying again ...");
		result = verifyMAC(cipherDigest, plainBytes, key);
		System.out.println("MAC is " + (result ? "right" : "wrong"));

	}

	/** auxiliary method to generate SecretKey */
	public static SecretKey generate() throws Exception {
		// generate an AES secret key
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		SecretKey key = keyGen.generateKey();

		return key;
	}

	/** auxiliary method to make the MAC */
	public static byte[] makeMAC(byte[] bytes, SecretKey key) throws Exception {

		Mac cipher = Mac.getInstance("HmacSHA1");
		cipher.init(key);
		byte[] cipherDigest = cipher.doFinal(bytes);

		System.out.println("CipherDigest:");
		System.out.println(printHexBinary(cipherDigest));

		return cipherDigest;
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyMAC(byte[] cipherDigest, byte[] bytes, SecretKey key) throws Exception {

		Mac cipher = Mac.getInstance("HmacSHA1");
		cipher.init(key);
		byte[] cipheredBytes = cipher.doFinal(bytes);
		return Arrays.equals(cipherDigest, cipheredBytes);
	}

}
