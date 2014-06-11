package ctd.test.rsa;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ctd.util.codec.RSAUtils;
import ctd.util.io.Bytes;

public class RsaTester {
	
	public static String[] splitString(String string, int len) {  
		int x = string.length() / len;  
		int y = string.length() % len;  
		int z = 0;  
		if (y != 0) {  
			z = 1;  
		}  
		String[] strings = new String[x + z];  
		String str = "";  
		for (int i=0; i<x+z; i++) {  
			if (i==x+z-1 && y!=0) {  
					str = string.substring(i*len, i*len+y);  
			}else{  
				str = string.substring(i*len, i*len+len);  
			}  
			strings[i] = str;  
		}  
		return strings;  
	}  
	
	public static byte[][] splitArray(byte[] data,int len){  
		int x = data.length / len;  
		int y = data.length % len;  
		int z = 0;  
		if(y!=0){  
			z = 1;  
		}  
		byte[][] arrays = new byte[x+z][];  
		byte[] arr;  
		for(int i=0; i<x+z; i++){  
			arr = new byte[len];  
			if(i==x+z-1 && y!=0){  
				System.arraycopy(data, i*len, arr, 0, y);  
		    }
			else{  
		    	System.arraycopy(data, i*len, arr, 0, len);  
		    }  
			arrays[i] = arr;  
		}  
		return arrays;  
	}


	
	public static void do1(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		String alg = "RSA";
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		keyPairGen.initialize(1024);  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		System.out.println(Bytes.bytes2hex(publicKey.getModulus().toByteArray()));
		System.out.println(Bytes.bytes2hex(privateKey.getPrivateExponent().toByteArray()));
		System.out.println(privateKey);
		
		 Cipher cipher = Cipher.getInstance(alg);  
		 cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
		
		 
		 int len = publicKey.getModulus().bitLength() / 8;
		 String[] datas = splitString(data, len - 11); 
		 
		 System.out.println(Bytes.bytes2hex(publicKey.getPublicExponent().toByteArray()));
		 
		 StringBuilder sb = new StringBuilder();
		 for (String s : datas) {  
			 sb.append(Bytes.bytes2hex(cipher.doFinal(s.getBytes())));  
		 }
		 String ens = sb.toString();
		 
		 
		 System.out.println(ens);
		 
		 ens = RSAUtils.encryptString(publicKey, data);
		 
		 System.out.println(ens);
		 
		 System.out.println(RSAUtils.decryptString(privateKey, ens));
	}
	
	public static void do2(){
		KeyPair kp = RSAUtils.generateKeyPair();
		
		RSAPublicKey publickey = (RSAPublicKey) kp.getPublic();
		//System.out.println(Bytes.bytes2hex(publickey.getModulus().toByteArray()));
		
		RSAPrivateKey privatekey = (RSAPrivateKey) kp.getPrivate();
		//System.out.println(Bytes.bytes2hex(privatekey.getPrivateExponent().toByteArray()));
		
		System.out.println(privatekey);
		
		String ens = RSAUtils.encryptString(privatekey, "helloworldhellowloworldhelloworldhelloworldhelloworldhelloworldhellowloworldhelloworldhelloworldhelloworldhelloworldhellowloworldhelloworldhelloworldhelloworldhelloworldhellowloworldhelloworldhelloworldhelloworld");
		System.out.println(ens);
		
		System.out.println(RSAUtils.decryptString(publickey, ens));
	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		//do2();
		do1("0d078c8a2f38b8f2ab17a7842ca928e7");
	}

}
