package ctd.test.rsa;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;


public class DotNetTester {

	public DotNetTester() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String publicKey = "AQAB";
        String modulus = "AIBJUwHWBoiwUxyl/OWOltgfVY0NUFI5tUyTu1r8y1W2CHdRA/etLuNrDZLHvZeRArPJb/Zo2dkuwCpxquI1lEDwSB+/pAVL/lFayctxlAEby4DpYzR6afUt7PPiFor8c4UTTz2VIkB+C30kJUViPJ2q7I1GKSCxhKpAawWr+8br";
        String privateKey = "YFQu01Md5ufYEMwHIWy3kiO9PsQ/iu6wV4cbCVCHcp3Ur5uTChjt+3C4aX44vCX0TII242WKJ432WCiwM1oeLq9W4A8OCsd8n6GHnhbvV+yN3Y884wQmFD0QyeS9kz2M9Zfvt8SuB9VuhVN7WBl2gai5qYjNBG2u/k0h5eLnyJE=";

        
        byte[] publicKeyBytes = Base64.decodeBase64(publicKey);
        byte[] modulusBytes = Base64.decodeBase64(modulus);
        byte[] privateKeyBytes = Base64.decodeBase64(privateKey);
        
        if(modulusBytes[0] == 0 && modulusBytes.length == 129){
        	byte[] temp = ArrayUtils.subarray(modulusBytes, 1, 129);
        	//System.out.println(Base64.encodeBase64String(temp));
        }
        
       
        
        PublicKey pubkey = RSACoder.toPublicKey(modulusBytes,publicKeyBytes);
        PrivateKey priKey = RSACoder.toPrivateKey(modulusBytes, privateKeyBytes);
        
        System.out.println(Base64.encodeBase64String(priKey.getEncoded()));
        System.out.println(priKey);
        
        String s = "hello";
    	byte[] data = s.getBytes();
        
    	//byte[] encryptData =  RSACoder.encrypt(priKey, data);
    	byte[] encryptData = Base64.decodeBase64("YWdVjgCIZVwYPO1fYAoRS9zGCaUrXpEfJmbhZRnNhLHkIez1EFsatUoKIovTNm6M5Ob7x7D6zJgNAs/w5bmpBqeSFOgon5Yb2STJ721YpYG9LhVoyBnPG4vcM1U9VFI7oQWAuemQfFcd7uBq2O1bt+iPmy1ZOZ9dxI1wo+0vfZU=");
    	//System.out.println(encryptData.length);
    	//encryptData = ArrayUtils.subarray(encryptData, 1, 129);
    	//System.out.println(Base64.encodeBase64String(encryptData));
    	
    	byte[] decryptData = RSACoder.decrypt(priKey, encryptData);
    	System.out.println(new String(decryptData));
	}

}
