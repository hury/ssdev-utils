package ctd.util.cert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.util.Date;

import org.apache.commons.io.FileUtils;

import ctd.util.codec.RSACoder;
import ctd.util.io.Bytes;

import sun.security.x509.*;


public class CertManager {
	private static String ROOT_CERT_ALIAS = "rootcert";
	
	private String keyStoreFilename;
	private KeyStore store;
	private char[] keyStorePassword;
	private X509Certificate rootCert;
	private PrivateKey rootCertPrivateKey;
	private String rootSubject;
	
	@SuppressWarnings("restriction")
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
	
	public X509Certificate getRootCert(){
		return rootCert;
	}
	
	public void writeToFile(X509Certificate c,String filename) throws Exception{
		File f = new File(filename);
        FileUtils.writeByteArrayToFile(f, c.getEncoded());
	}
	
	@SuppressWarnings("restriction")
	public X509Certificate createUserCert(String subject,int days) throws Exception{
		System.out.println(rootCert.getSubjectDN().toString());
		byte certbytes[] = rootCert.getEncoded();
        X509CertImpl x509certimpl = new X509CertImpl(certbytes);
        X509CertInfo x509certinfo = (X509CertInfo)x509certimpl.get("x509.info");
        
        KeyPair kp = RSACoder.initKeyPair();
        CertificateX509Key key = new CertificateX509Key(kp.getPublic());
        
        X500Name x500Subject = new X500Name(subject);
        x509certinfo.set("issuer.dname",rootCert.getIssuerDN());
        x509certinfo.set("subject.dname", x500Subject);
        x509certinfo.set(X509CertInfo.KEY,key);
        
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
	
	public void putIntoStore(String alias,PrivateKey key,X509Certificate c) throws Exception{
		store.setKeyEntry(alias, key, keyStorePassword, new Certificate[]{c});
	}
	
	public X509Certificate getCertFromStore(String alias) throws Exception{
		return (X509Certificate) store.getCertificate(alias);
	}
	
	public PrivateKey getPrivateKeyFromStore(String alias) throws Exception{
		return (PrivateKey) store.getKey(alias,keyStorePassword);
	}
	
	public void setKeyStorePassword(String password){
		this.keyStorePassword = password.toCharArray();
	}
	
	public void setKeyStoreFilename(String filename){
		this.keyStoreFilename = filename;
	}
	
	public void setRootSubject(String subject){
		this.rootSubject = subject;
	}
	
	public  PrivateKey getRootCertPrivateKey(){
		return rootCertPrivateKey;
	}
	
	public static void main(String[] args) throws Exception{
		CertManager cert = new CertManager();
		cert.setKeyStoreFilename("D:\\my.keyStore");
		cert.setRootSubject("OU=BSoft,L=HangZhou,ST=Zhejiang,C=CN");
		cert.setKeyStorePassword("123456");
		cert.init();
		
		X509Certificate c = cert.createUserCert("CN=Sophie,OU=BSoft,L=HangZhou,ST=Zhejiang,C=CN", 200);
		cert.writeToFile(c, "D:\\sophie2.crt");
		
		cert.writeToFile(cert.getRootCert(), "D:\\root.crt");
        
		
		
		cert.putIntoStore("sophie", cert.getRootCertPrivateKey(), c);
        
		
		
		PrivateKey key =cert.getPrivateKeyFromStore("sophie");
		System.out.println(Bytes.bytes2hex(c.getPublicKey().getEncoded()));
		System.out.println(Bytes.bytes2hex(cert.getRootCert().getPublicKey().getEncoded()));
		
		
	}
}
