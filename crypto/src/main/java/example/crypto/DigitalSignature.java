package example.crypto;

//provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignatureException;

/** Generate a digital signature using the signature object provided by Java */
public class DigitalSignature {

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

		// generate RSA KeyPair
		KeyPair key = generate();

		// make digital signature
		System.out.println("Signing ...");
		byte[] cipherDigest = makeDigitalSignature(plainBytes, key);

		// verify the signature
		System.out.println("Verifying ...");
		boolean result = verifyDigitalSignature(cipherDigest, plainBytes, key);
		System.out.println("Signature is " + (result ? "right" : "wrong"));

		// data modification ...
		plainBytes[3] = 12;
		System.out.println("Tampered bytes: (look closely around the 7th hex character)");
		System.out.println(printHexBinary(plainBytes));

		// verify the signature
		System.out.println("Verifying ...");
		result = verifyDigitalSignature(cipherDigest, plainBytes, key);
		System.out.println("Signature is " + (result ? "right" : "wrong"));
	}

	/** auxiliary method to generate KeyPair */
	public static KeyPair generate() throws Exception {
		// generate an RSA key pair
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();

		return key;
	}

	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, KeyPair keyPair) throws Exception {

		// get a signature object using the SHA-1 and RSA combo
		// and sign the plaintext with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(keyPair.getPrivate());
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, KeyPair keyPair) throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(keyPair.getPublic());
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying " + se);
			return false;
		}
	}
}
