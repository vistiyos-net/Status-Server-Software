package net.vistiyos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Encriptar {
	
	private static final String CONFIGURATION = "config/config.xml";
	
	static{
	      org.apache.xml.security.Init.init();
	}

	private static SecretKey GenerateKeyEncryptionKey() throws Exception{
	      String jceAlgorithmName = "DESede";
	      KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithmName);
	      SecretKey keyEncryptKey = keyGenerator.generateKey();
	      return keyEncryptKey;
	}

	private static void storeKeyFile(Key keyEncryptKey) throws IOException{
	      byte[] keyBytes = keyEncryptKey.getEncoded();
	      File keyEncryptKeyFile = new File("keyEncryptKey");
	      FileOutputStream outStream = new FileOutputStream(keyEncryptKeyFile);
	      outStream.write(keyBytes);
	      outStream.close();
	}

	private static SecretKey GenerateSymmetricKey() throws Exception {
	      String jceAlgorithmName = "AES";
	      KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithmName);
	      keyGenerator.init(128);
	      return keyGenerator.generateKey();
	}

	private static void writeEncryptedDocToFile(Document doc,String fileName) throws Exception{
	      File encryptionFile = new File(fileName);
	      FileOutputStream outStream = new FileOutputStream(encryptionFile);
	      TransformerFactory factory = TransformerFactory.newInstance();
	      Transformer transformer = factory.newTransformer();
	      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
	      DOMSource source = new DOMSource(doc);
	      StreamResult result = new StreamResult(outStream);
	      transformer.transform(source, result);
	      outStream.close();
	}
	
	public static void encriptar(Document documento) throws Exception{
		 Key symmetricKey = GenerateSymmetricKey();
	     Key keyEncryptKey = GenerateKeyEncryptionKey();
	     storeKeyFile(keyEncryptKey);
	     XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES_KeyWrap);
	     keyCipher.init(XMLCipher.WRAP_MODE, keyEncryptKey);
	     EncryptedKey encryptedKey = keyCipher.encryptKey(documento,symmetricKey);
	     Element rootElement = documento.getDocumentElement();
	     Element elementToEncrypt = rootElement;
	     XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
	     xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);
	     EncryptedData encryptedDataElement = xmlCipher.getEncryptedData();
	     KeyInfo keyInfo = new KeyInfo(documento);
	     keyInfo.add(encryptedKey);
	     encryptedDataElement.setKeyInfo(keyInfo);
	     xmlCipher.doFinal(documento, elementToEncrypt, true);
	     writeEncryptedDocToFile(documento, CONFIGURATION);
	}
}
