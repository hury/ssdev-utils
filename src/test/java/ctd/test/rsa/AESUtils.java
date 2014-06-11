package ctd.test.rsa;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import ctd.util.io.Bytes;

public class AESUtils {
	
	private static final String KEY_ALGORITHM = "AES";   
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	public static byte[] initSecretKey() throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(128);
		SecretKey  secretKey = kg.generateKey();
		
		return secretKey.getEncoded();
	}
	
	 public static Key toKey(byte[] key){
		 return new SecretKeySpec(key, KEY_ALGORITHM);   
	 }
	 
	 public static byte[] encrypt(byte[] data,Key key) throws Exception{   
		 return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);   
	}
	
	 public static byte[] encrypt(byte[] data,byte[] key) throws Exception{   
		return encrypt(data, toKey(key),DEFAULT_CIPHER_ALGORITHM);   
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
	 
	 private static String  showByteArray(byte[] data){   
		 if(null == data){   
		             return null;   
		 }   
		 StringBuilder sb = new StringBuilder("{");   
		 for(byte b:data){   
			 sb.append(b).append(",");   
		 }   
		 sb.deleteCharAt(sb.length()-1);   
		 sb.append("}");   
		 return sb.toString();   
	}   

	 
	public static void main(String[] args) throws Exception{
		byte[] key = new String("1234567890123456").getBytes();
		System.out.println(Base64.encodeBase64String(key));
		
		String s = "hello";
		
		byte[] data = s.getBytes();
		byte[] binaryData = encrypt(data, key); 
		
		String result = Base64.encodeBase64String(binaryData);
		System.out.println(result);
		
		data = Base64.decodeBase64(result);
		
		result = new String(decrypt(binaryData, key));
		System.out.println(result);
	}

}
