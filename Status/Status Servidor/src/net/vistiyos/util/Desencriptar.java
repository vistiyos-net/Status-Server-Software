package net.vistiyos.util;

import java.io.File;
import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.apache.xml.security.utils.JavaUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Desencriptar {
	
	private static final String CONFIGURATION = "config/config.xml";
	
	static{
		org.apache.xml.security.Init.init();
	}

	private static Document loadEncryptedFile(String fileName) throws Exception{
	      File encryptedFile = new File(fileName);
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      dbf.setNamespaceAware(true);
	      DocumentBuilder builder = dbf.newDocumentBuilder();
	      Document document = builder.parse(encryptedFile);
	      return document;
	}

	private static SecretKey loadKeyEncryptionKey() throws Exception {
	      String fileName = "keyEncryptKey";
	      String jceAlgorithmName = "DESede";
	      DESedeKeySpec keySpec = new DESedeKeySpec(JavaUtils.getBytesFromFile(fileName));
	      SecretKeyFactory skf = SecretKeyFactory.getInstance(jceAlgorithmName);
	      SecretKey key = skf.generateSecret(keySpec);
	      return key;
	}
	
	public static Document desencriptar() throws Exception{
	      Document document = loadEncryptedFile(CONFIGURATION);
	      String namespaceURI = EncryptionConstants.EncryptionSpecNS;
	      String localName = EncryptionConstants._TAG_ENCRYPTEDDATA;
	      Element encryptedDataElement = (Element)document.getElementsByTagNameNS(namespaceURI,localName).item(0);
	      Key keyEncryptKey = loadKeyEncryptionKey();
	      XMLCipher xmlCipher = XMLCipher.getInstance();
	      xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
	      xmlCipher.setKEK(keyEncryptKey);
	      xmlCipher.doFinal(document, encryptedDataElement);
	      return document;
	}
}
