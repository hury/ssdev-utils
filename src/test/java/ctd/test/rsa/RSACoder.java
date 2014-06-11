package ctd.test.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import ctd.util.io.Bytes;

public class RSACoder {
	private static final String KEY_ALGORITHM = "RSA";
	private static final String DEFAULT_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static KeyPairGenerator keyPairGen;
    private static KeyFactory keyFactory;
    
    static{
    	 try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(1024);
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    	}
    	catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
    	
    }
    
    public static KeyPair initKeyPair() throws Exception {
        return keyPairGen.generateKeyPair();
    }
    
    public static PublicKey toPublicKey(byte[] encodedKeyBytes) throws Exception{
    	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }
    
    public static PublicKey toPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(modulus),new BigInteger(publicExponent));
        return keyFactory.generatePublic(keySpec);
    }
    
    public static PrivateKey toPrivateKey(byte[] encodedKeyBytes) throws Exception{
    	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
    
    public static PrivateKey toPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(privateExponent));
        return keyFactory.generatePrivate(keySpec);
    }
    
    public static byte[] sign(byte[] data, byte[] privateKeyBytes) throws Exception {
        PrivateKey privateK = toPrivateKey(privateKeyBytes);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return signature.sign();
    }
    
    public static boolean verify(byte[] data, byte[] publicKeyBytes, byte[] signBytes) throws Exception {
        PublicKey publicK = toPublicKey(publicKeyBytes);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(signBytes);
    }
    
    public static byte[] encryptByPrivateKey(byte[] keyBytes,byte[] data) throws Exception{
    	return encrypt(toPrivateKey(keyBytes),data);
    }
    
    public static byte[] encryptByPublicKey(byte[] keyBytes,byte[] data) throws Exception{
    	return encrypt(toPublicKey(keyBytes),data);
    }
    
    public static byte[] encrypt(Key key, byte[] data)throws Exception{
    	return encrypt(key,data,DEFAULT_CIPHER_ALGORITHM);
    }
    
    public static byte[] encrypt(Key key, byte[] data,String defaultCipherAlgorithm) throws Exception{
    	Cipher ci = Cipher.getInstance(defaultCipherAlgorithm);
		ci.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] encryptedData = null;
		int len = data.length;
		int offSet = 0;
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len - offSet > 0) {
            if (len - offSet > MAX_ENCRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } 
            else {
                cache = ci.doFinal(data, offSet, len - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        encryptedData = out.toByteArray();
        out.close();

		return encryptedData;
    }
    
    public static byte[] decryptByPrivateKey(byte[] keyBytes,byte[] data) throws Exception{
    	return decrypt(toPrivateKey(keyBytes),data);
    }
    
    public static byte[] decryptByPublicKey(byte[] keyBytes,byte[] data) throws Exception{
    	return decrypt(toPublicKey(keyBytes),data);
    }
    
    public static byte[] decrypt(Key key, byte[] data)throws Exception{
    	return decrypt(key,data,DEFAULT_CIPHER_ALGORITHM);
    }
    
    
    public static byte[] decrypt(Key key, byte[] data,String defaultCipherAlgorithm) throws Exception{
    	Cipher ci = Cipher.getInstance(defaultCipherAlgorithm);
		ci.init(Cipher.DECRYPT_MODE, (Key) key);
		
		byte[] decryptedData = null;
		int len = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        System.out.println("len=" + len);
        while (len - offSet > 0) {
            if (len - offSet > MAX_DECRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = ci.doFinal(data, offSet, len - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }
    
    
    public static void main(String[] args) throws Exception{
    	KeyPair kp = initKeyPair();
    	System.out.println("===========privateKey====================");
    	RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate(); 
    	byte[] m = privateKey.getModulus().toByteArray();
    	byte[] privateExponent = privateKey.getPrivateExponent().toByteArray();
    	byte[] privateBytes = privateKey.getEncoded();
    	System.out.println(privateKey);
    	System.out.println(Base64.encodeBase64String(privateExponent));
    	System.out.println(Base64.encodeBase64String(privateBytes));
    	//System.out.println(Bytes.bytes2hex(privateExponent));
    	//System.out.println(Bytes.bytes2hex(((RSAPrivateKey)kp.getPrivate()).getPrivateExponent().toByteArray()));
    	System.out.println("===========publicKey====================");
    	RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
    	byte[] publicExponent = publicKey.getPublicExponent().toByteArray();
    	byte[] publicBytes = publicKey.getEncoded();
    	//System.out.println(publicKey);
    	//System.out.println(Bytes.bytes2hex(publicExponent));
    	System.out.println(Base64.encodeBase64String(publicExponent));
    	System.out.println(Base64.encodeBase64String(m));
    	//System.out.println(Bytes.bytes2hex(m));
    	//System.out.println(Bytes.bytes2hex(((RSAPrivateKey)kp.getPrivate()).getModulus().toByteArray()));
    	
    	String s = "hello";
    	byte[] data = s.getBytes();
    	
    	//byte[] encryptData = encrypt(toPublicKey(m,publicExponent), data);
    	byte[] encryptData = encrypt(toPrivateKey(m,privateExponent), data);
    	
    	System.out.println("===========encrptData====================");
    	System.out.println(Bytes.bytes2hex(encryptData));
    	
    	
    	//encryptData = Bytes.hex2bytes("927F96E0B0B114F2F621B0E2422F4085FEE9C194AC58F0C93C5A2F3F73920C06FEE71BB0D98E48889762A259B56424D4EECC487083D4480541B6B62BC68D82E3596CA5D934A9DF446B4B12583EAE53E1C72992D2597A043E90BE96CE87AB6EF275D4D03B54AA1F2FE3D4F8FABC882CB36A5336664AAB369CF23065E4001849EA");
    	
    	
    	
    	//byte[] decryptData = decrypt(toPrivateKey(m,privateExponent), encryptData);
    	byte[] decryptData = decrypt(toPublicKey(m,publicExponent), encryptData);
    	
    	System.out.println(new String(decryptData));
    }
}
