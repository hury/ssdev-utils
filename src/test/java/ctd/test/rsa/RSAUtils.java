package ctd.test.rsa;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.util.io.Bytes;


public class RSAUtils {
	  private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);
	  private static final String ALGORITHOM = "RSA";
	  private static final int KEY_SIZE = 1024;
	  private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
	  private static KeyPairGenerator keyPairGen = null;
	  private static KeyFactory keyFactory = null;
	  private static KeyPair oneKeyPair = null;
	    
	  static{
		  try {
			keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		 } 
		 catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		 }
	  }
	  
	  public static synchronized KeyPair generateKeyPair() {
	        try {
	            keyPairGen.initialize(KEY_SIZE, new SecureRandom(DateFormatUtils.format(new Date(), "yyyyMMdd").getBytes()));
	            oneKeyPair = keyPairGen.generateKeyPair();
	            return oneKeyPair;
	        } 
	        catch (InvalidParameterException ex) {
	            LOGGER.error("KeyPairGenerator does not support a key length of " + KEY_SIZE + ".", ex);
	        }
	        catch (NullPointerException ex) {
	            LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.",ex);
	        }
	        return null;
	  }
	  
	  public static String encryptString(RSAKey key, String plaintext) throws Exception{
		  Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		  ci.init(Cipher.ENCRYPT_MODE, (Key) key);
		  
		  int len = key.getModulus().bitLength() / 8;
		  String[] datas = splitString(plaintext, len - 11);
		  
		  StringBuilder sb = new StringBuilder();
		  for (String s : datas) {  
			 sb.append(Bytes.bytes2hex(ci.doFinal(s.getBytes())));  
		  }
		  return sb.toString();
	  }
	  
	  private static String[] splitString(String string, int len) {  
			int x = string.length() / len;  
			int y = string.length() % len;  
			int z = 0;  
			if (y != 0) {  
				z = 1;  
			}  
			String[] strings = new String[x + z];  
			String str = "";  
			for (int i = 0; i < x+z; i ++) {  
				if (i == x + z - 1 && y != 0) {  
						str = string.substring(i*len, i*len+y);  
				}else{  
					str = string.substring(i*len, i*len+len);  
				}  
				strings[i] = str;  
			}  
			return strings;  
		}
}
