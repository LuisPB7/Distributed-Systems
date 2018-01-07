package example.crypto;

// helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Example of how to insert and retrieve cipher data to and from XML, using base
 * 64 encoding to represent ciphered data as text
 */
public class XmlCipher {

	public static final String XML = "<message>" + "<body>There and Back Again</body>" + "</message>";
	private static final int AES_IV_LENGTH = 16; // AES initialization vector
													// length [bytes]

	public static void main(String[] args) throws Exception {

		// parse XML document
		//
		InputStream xmlInputStream = new ByteArrayInputStream(XML.getBytes());

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		System.out.println("Parsing XML document from string bytes...");
		Document xmlDocument = documentBuilder.parse(xmlInputStream);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");

		System.out.println("XML document contents:");
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(System.out));
		System.out.println();

		// retrieve body text
		//
		System.out.print("Body text: ");
		Node bodyNode = null;
		for (Node node = xmlDocument.getDocumentElement().getFirstChild(); node != null; node = node.getNextSibling()) {

			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("body")) {
				bodyNode = node;
				break;
			}
		}
		if (bodyNode == null) {
			throw new Exception("Body node not found!");
		}

		String plainText = bodyNode.getTextContent();
		System.out.println(plainText);
		byte[] plainBytes = plainText.getBytes();

		// remove body node
		xmlDocument.getDocumentElement().removeChild(bodyNode);

		// cipher body
		//

		// generate a secret key
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		Key key = keyGen.generateKey();

		// get an AES cipher object
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// sample AES initialization vector
		byte[] iv = new byte[AES_IV_LENGTH];
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.nextBytes(iv);
		IvParameterSpec ips = new IvParameterSpec(iv);
		// encrypt using the key and the plaintext
		cipher.init(Cipher.ENCRYPT_MODE, key, ips);
		byte[] cipherBytes = cipher.doFinal(plainBytes);
		System.out.print("Ciphered bytes: ");
		System.out.println(printHexBinary(cipherBytes));

		// encoding binary data with base 64
		String cipherText = printBase64Binary(cipherBytes);
		System.out.print("Ciphered bytes in Base64: ");
		System.out.println(cipherText);

		// create the element
		Element cipherBodyElement = xmlDocument.createElement("cipherBody");
		Text text = xmlDocument.createTextNode(cipherText);
		cipherBodyElement.appendChild(text);
		// append nodes to document
		xmlDocument.getDocumentElement().appendChild(cipherBodyElement);

		System.out.println("XML document with cipher body:");
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(System.out));
		System.out.println();

		// decipher body
		//
		String cipherBodyText = cipherBodyElement.getTextContent();

		System.out.print("Cipher body text: ");
		System.out.println(cipherBodyText);

		// decoding string in base 64
		byte[] cipherBodyBytes = parseBase64Binary(cipherBodyText);
		System.out.print("Ciphered bytes: ");
		System.out.println(printHexBinary(cipherBodyBytes));

		// get an AES cipher object
		Cipher newCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// decrypt using the key and the ciphertext
		newCipher.init(Cipher.DECRYPT_MODE, key, ips);
		byte[] newPlainBytes = newCipher.doFinal(cipherBodyBytes);
		System.out.print("Deciphered bytes: ");
		System.out.println(printHexBinary(newPlainBytes));
		String newPlainText = new String(newPlainBytes);
		System.out.print("Text: ");
		System.out.println(newPlainText);

		// remove cipher body node
		xmlDocument.getDocumentElement().removeChild(cipherBodyElement);

		// create the element
		Element bodyElement = xmlDocument.createElement("body");
		Text newText = xmlDocument.createTextNode(newPlainText);
		bodyElement.appendChild(newText);
		// append nodes to document
		xmlDocument.getDocumentElement().appendChild(bodyElement);

		System.out.println("XML document with new body:");
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(System.out));
		System.out.println();
	}
}
