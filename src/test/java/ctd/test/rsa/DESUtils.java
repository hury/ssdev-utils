package ctd.test.rsa;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import ctd.util.io.Bytes;

public class DESUtils {
	private static final String KEY_ALGORITHM = "DES";   
	private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	private static final SecretKeyFactory skf;
	
	static{
		try {
			skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		} 
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
		
	}
	
	 public static byte[] initSecretKey() throws Exception{   
		 KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);    
		 kg.init(56);   
		 SecretKey  secretKey = kg.generateKey();   
		 return secretKey.getEncoded();   
	}
	 
	public static Key toKey(byte[] key) throws Exception{   
		 DESKeySpec dks = new DESKeySpec(key);    
		 SecretKey  secretKey = skf.generateSecret(dks);   
		 return secretKey;   
	}
	 
	 public static byte[] encrypt(byte[] data,Key key) throws Exception{   
		 return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);   
	 }
	 
	 public static byte[] encrypt(byte[] data,byte[] key) throws Exception{   
		 return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);   
	 }
	 
	 public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{   
		 Key k = toKey(key);   
		 return encrypt(data, k, cipherAlgorithm);   
	 }
	 
	 public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{   
		 Cipher cipher = Cipher.getInstance(cipherAlgorithm);     
		 cipher.init(Cipher.ENCRYPT_MODE, key);   
		 return cipher.doFinal(data);   
	} 
	
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{   
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);   
	}
	
	public static byte[] decrypt(byte[] data,Key key) throws Exception{   
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);   
	}
	
	public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{     
		Key k = toKey(key);   
		return decrypt(data, k, cipherAlgorithm);   
	}
	
	public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{    
		 Cipher cipher = Cipher.getInstance(cipherAlgorithm);   
		 cipher.init(Cipher.DECRYPT_MODE, key);   
		 return cipher.doFinal(data);   
	 } 

	public static void main(String[] args) throws Exception{
		byte[] key = initSecretKey();
		System.out.println(Base64.encodeBase64String(key));
		System.out.println("============================");
		String data = "hello";
		
		byte[] encryptData = encrypt(data.getBytes(), key);
		System.out.println(Bytes.bytes2hex(encryptData));
		System.out.println(Base64.encodeBase64String(encryptData));
		
		byte[] decryptData = decrypt(encryptData, key);
		System.out.println(new String(decryptData)); 

	}
	
}
