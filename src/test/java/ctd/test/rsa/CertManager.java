package ctd.test.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import sun.security.x509.*;

@SuppressWarnings("restriction")
public class CertManager {
	private static String ROOT_CERT_ALIAS = "rootcert";
	
	private String keyStoreFilename;
	private KeyStore store;
	private char[] keyStorePassword;
	private X509Certificate rootCert;
	private PrivateKey rootCertPrivateKey;
	private String rootSubject;
	
	public void init(){
		try{
			File f = new File(keyStoreFilename);
			store = KeyStore.getInstance("JKS");
		        
			if(f.exists()){
				store.load(new FileInputStream(f), keyStorePassword);
			}
			else{
				store.load(null,keyStorePassword);
				store.store(new FileOutputStream(f),keyStorePassword);
			}
			
			if(store.containsAlias(ROOT_CERT_ALIAS)){
				rootCertPrivateKey = (PrivateKey) store.getKey(ROOT_CERT_ALIAS, keyStorePassword);
				rootCert = (X509Certificate) store.getCertificate(ROOT_CERT_ALIAS);
			}
			else{
				CertAndKeyGen cak = new CertAndKeyGen("RSA","MD5WithRSA",null);
				cak.generate(1024);
				rootCertPrivateKey = cak.getPrivateKey();
				X500Name subject = new X500Name(rootSubject);
				rootCert = cak.getSelfCertificate(subject,60*60*24*3650);
				store.setKeyEntry(ROOT_CERT_ALIAS, rootCertPrivateKey, keyStorePassword, new Certificate[]{rootCert});
				store.store(new FileOutputStream(f),keyStorePassword);
			}
		}
		catch(Exception e){
			throw new IllegalStateException(e);
		}
	}
	
	public X509Certificate createUserCert(String subject,int days) throws Exception{
		byte certbytes[] = rootCert.getEncoded();
        X509CertImpl x509certimpl = new X509CertImpl(certbytes);
        X509CertInfo x509certinfo = (X509CertInfo)x509certimpl.get("x509.info");
        x509certinfo.set("issuer.dname",rootCert.getIssuerDN());
        x509certinfo.set("subject.dname", new X500Name(subject));
        
        Date bdate = new Date();
        Date edate = new Date();
        edate.setTime(bdate.getTime() + days * 1000L * 24L * 60L * 60L);
        
        CertificateValidity certificatevalidity = new CertificateValidity(bdate, edate);
        x509certinfo.set("validity", certificatevalidity);
        x509certinfo.set("serialNumber", new CertificateSerialNumber((int)(new Date().getTime() / 1000L)));
        
        CertificateVersion cv = new CertificateVersion(CertificateVersion.V3);
        x509certinfo.set(X509CertInfo.VERSION,cv);
        
        X509CertImpl userCert = new X509CertImpl(x509certinfo);
        userCert.sign(rootCertPrivateKey, "MD5WithRSA");
        
        return userCert;
	}
	
	
	private static CertAndKeyGen cak;
	
	static{
		
		try {
			cak = new CertAndKeyGen("RSA","MD5WithRSA",null);
			cak.generate(1024);
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		} 
	}
	
	
	public static X509Certificate createUserCert(X509Certificate rootCert,X500Name subject,int days) throws Exception{
		byte certbytes[] = rootCert.getEncoded();
        X509CertImpl x509certimpl = new X509CertImpl(certbytes);
        X509CertInfo x509certinfo = (X509CertInfo)x509certimpl.get("x509.info");
        x509certinfo.set("issuer.dname",rootCert.getIssuerDN());
        x509certinfo.set("subject.dname", subject);
        
        Date bdate = new Date();
        Date edate = new Date();
        edate.setTime(bdate.getTime() + days * 1000L * 24L * 60L * 60L);
        
        CertificateValidity certificatevalidity = new CertificateValidity(bdate, edate);
        x509certinfo.set("validity", certificatevalidity);
        x509certinfo.set("serialNumber", new CertificateSerialNumber((int)(new Date().getTime() / 1000L)));
        
        CertificateVersion cv = new CertificateVersion(CertificateVersion.V3);
        x509certinfo.set(X509CertInfo.VERSION,cv);
        
        X509CertImpl userCert = new X509CertImpl(x509certinfo);
//        userCert.sign(rootCert.getPrivateKey(), "MD5WithRSA");
//        userCert.verify(rootCert.getPublicKey());
        
        return userCert;
        

	}
	

	public static void main(String[] args) throws Exception{
		X509Certificate rootCert = null;
	
		File f = new File("D:\\root.crt");
		if(f.exists()){
			 FileInputStream ins = new FileInputStream(f); 
		     CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		     rootCert = (X509Certificate)certFactory.generateCertificate(ins);
		}
		else{
			X500Name subject = new X500Name("OU=BSoft,L=HangZhou,ST=Zhejiang,C=CN");
			rootCert = cak.getSelfCertificate(subject,60*60*24*365);
			FileUtils.writeByteArrayToFile(f, rootCert.getEncoded());
		}
        	
        
        
        X500Name subject2 = new X500Name("CN=Sophie,OU=BSoft,L=HangZhou,ST=Zhejiang,C=CN");
        X509Certificate userCert =  createUserCert(rootCert,subject2,100);
        
        System.out.println(userCert.getPublicKey());
        
        f = new File("D:\\sean.crt");
        FileUtils.writeByteArrayToFile(f, userCert.getEncoded());
        
      
        
        KeyStore store = KeyStore.getInstance("JKS");
        store.load(null, "123456".toCharArray());
        
        store.setKeyEntry("cert1",cak.getPrivateKey(), "123456".toCharArray(),new Certificate[]{userCert});

        
        OutputStream os = new FileOutputStream("d:\\my.keyStore");
        store.store(os, "123456".toCharArray());
        os.close();
        
     
        
        store.load(new FileInputStream("d:\\my.keyStore"),"123456".toCharArray());
        
        X509Certificate c = (X509Certificate) store.getCertificate("cert1");
       
       
       System.out.println(c.getSubjectDN().toString());
	}
}
