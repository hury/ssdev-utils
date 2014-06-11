package ctd.test.rsa;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKX509CertificateFactory;
import org.bouncycastle.jce.provider.PEMUtil;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Base64Encoder;

import sun.misc.BASE64Encoder;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.Extension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

@SuppressWarnings("restriction")
public class Cert {

	public static void main(String[] args) throws Exception {
		StringBuffer prikey = new StringBuffer();
		prikey.append("-----BEGIN RSA PRIVATE KEY-----\r\n");
		prikey.append("MIICWwIBAAKBgQCbaGng2Sq6Ra3LDlqVDcUq7ciZ6yofeFHbYfhbWideJ7cQXTsm\r\n");
		prikey.append("VnolrJtUZBH+qLOWHO7KS7fIJbAr79ZL+govf2SgzghYVbwj/QqALxDJMcaYlAps\r\n");
		prikey.append("MgO0wmgKUxETbXM0d0BfDJHLPImpiFJTtNPZFBvdlIqqKCKkF5dfGTRacwIBAwKB\r\n");
		prikey.append("gGea8UCQxybZHode5w4JLhyehbvyHBT64Tzr+ueRb5Qaegro0hmO/BkdvOLtYVRw\r\n");
		prikey.append("d7lonzGHz9rDysf1OYf8BslK6w87Y8rRtY/yMUA8APNusJlWcShH6EvAStkAgu07\r\n");
		prikey.append("ZruywvLeUfjzNgpl9/IAEDwVR+8CzejnlvpZzn2IwKp7AkEAy91tRFePp+GnQr9Q\r\n");
		prikey.append("4QgS5Xpvsbm7XDGB/pFitD31g1uhjFfLb4FYyc3Vc+ZXM0yAivH0JpUpZDJG0TS8\r\n");
		prikey.append("AIUuRwJBAMMmnLCbGHNKJM8BTvQHSSZDQuCm9Gn1wSNmYh4OuhD9eV7EJ0+VfQXq\r\n");
		prikey.append("xHt4EYFu/mk+AunAo2iN5b+s5suOLHUCQQCH6POC5Qpv68TXKjXrWrdDpvUhJnzo\r\n");
		prikey.append("IQFUYOx4KU5Xkmuy5TJKVjsxM+OimY93iFWx9qLEY3DtdtngzdKrA3QvAkEAghm9\r\n");
		prikey.append("yxIQTNwYigDfTVowxCzXQG9Nm/krbO7sFAnRYKj7lILE37j+A/HYUlALq59URilX\r\n");
		prikey.append("RoBs8F6ZKnNEh7QdowJAOyq4KCMEFLCKTtGEHNFaULNU5fRzaAapTiIqKPpyc6eU\r\n");
		prikey.append("HfFbHkREVFIKe9BJldRIDGdxhtocCpkbH1M6Bu7LCQ==\r\n");
		prikey.append("-----END RSA PRIVATE KEY-----\r\n");
		String cers = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUI2RENDQVZHZ0F3SUJBUUlCR0RBTkJna3Foa2lHOXcwQkFRUUZBREE3TVRrd0NBWURWUVFNREFFMk1Bb0cKQTFVRURRd0ROVEF3TUF3R0ExVUVCZ3dGUTBoSlRrRXdFd1lEVlFRRERBeE5jM1JoY2kxQ2FXRnZjV2t3SGhjTgpNVEV3TnpFMk1ESXlPVEF6V2hjTk1Ua3dNVEExTURJeU9UQXpXakE3TVRrd0NBWURWUVFNREFFMk1Bb0dBMVVFCkRRd0ROVEF3TUF3R0ExVUVCZ3dGUTBoSlRrRXdFd1lEVlFRRERBeE5jM1JoY2kxQ2FXRnZjV2t3Z1owd0RRWUoKS29aSWh2Y05BUUVCQlFBRGdZc0FNSUdIQW9HQkFKdG9hZURaS3JwRnJjc09XcFVOeFNydHlKbnJLaDk0VWR0aAorRnRhSjE0bnR4QmRPeVpXZWlXc20xUmtFZjZvczVZYzdzcEx0OGdsc0N2djFrdjZDaTkvWktET0NGaFZ2Q1A5CkNvQXZFTWt4eHBpVUNtd3lBN1RDYUFwVEVSTnRjelIzUUY4TWtjczhpYW1JVWxPMDA5a1VHOTJVaXFvb0lxUVgKbDE4Wk5GcHpBZ0VETUEwR0NTcUdTSWIzRFFFQkJBVUFBNEdCQUNDZjl3SGFoc2lLL0R0ejFCNG0yemFEaTNhSwpUcUlsTjB4cGxyd0J0TGRjcFlESWZ4NTV3S0NJemlCbzEwY2h5QzlDL1VRS2ZUOWtsc1VoTkRqUDg4cWYyWGo5Ci9DN0ZHR3czWVFzbXJkQy9zREJnODRJUWFlSG5Md2dPL3dkSUR5ajJDdExDd0lWMFRGRU44bUZVWFZPNm9wZjEKZDNjUExuYU1qb05NMEtwbgotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==";

		String source = "4867B6969A70A0E6C58904EAB3332BEB82CBF8DEE2D84AF42036C7055B2F5E53";
		String dest = "HVAKBY6mTEmX43Yzf3ZHwSUeBKJP7Hg7rGjoCrHk+KTBSr+nuJw5bxHQIzPXCRgIvpp8wHsWDyNjPm5X8hlRnzkZsNmhtCEJ4iUm8VUj/sG0OOLqq2JCRr4SD+eEYwxLNQqouG57pzGmDC3JcvSISdcKoL6pzXgsebv9W+rHEqQ=";
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		PEMReader r = new PEMReader(new InputStreamReader(
				new ByteArrayInputStream(prikey.toString().getBytes())));
		Object readObject = r.readObject();
		KeyPair keyPair = (KeyPair) readObject;
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
		StringBuffer sourceEncrypt = new StringBuffer();
		byte[] sourceByte = source.getBytes();
		for (int i = 0; i < sourceByte.length; i += 117) {
			byte[] temp = new byte[sourceByte.length - i >= 117 ? 117
					: sourceByte.length - i];
			System.arraycopy(sourceByte, i, temp, 0, temp.length);
			sourceEncrypt.append(new String(Base64.encodeBase64(cipher
					.doFinal(temp))));
		}
		System.out.println(dest);
		System.out.println(sourceEncrypt.toString());

		JDKX509CertificateFactory x509cerfac = new JDKX509CertificateFactory();
		Certificate engineGenerateCertificate = x509cerfac
				.engineGenerateCertificate(new ByteArrayInputStream(Base64
						.decodeBase64(cers)));
		X509Certificate x509cer = (X509Certificate) engineGenerateCertificate;
		Cipher cipher1 = Cipher.getInstance("RSA");
		cipher1.init(Cipher.DECRYPT_MODE, x509cer);
		byte[] destByte = Base64.decodeBase64(sourceEncrypt.toString());
		// byte[] destByte=Base64.decodeBase64(dest.toString());
		System.out.println(destByte.length);
		StringBuffer destDecrypt = new StringBuffer();
		for (int i = 0; i < destByte.length; i += 128) {
			byte[] temp = new byte[destByte.length - i >= 128 ? 128
					: destByte.length - i];
			System.arraycopy(destByte, i, temp, 0, temp.length);
			destDecrypt.append(new String(cipher1.doFinal(temp)));
		}
		System.out.println(source);
		System.out.println(destDecrypt.toString());

		PEMWriter pw = new PEMWriter(
				new OutputStreamWriter(
						new FileOutputStream(
								"C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/test.cer")));
		pw.writeObject(engineGenerateCertificate);
		pw.flush();
		pw.close();

		PEMWriter pw1 = new PEMWriter(
				new OutputStreamWriter(
						new FileOutputStream(
								"C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/test2.prikey")));
		java.security.KeyPairGenerator kpg = KeyPairGenerator
				.getInstance("RSA");
		kpg.initialize(1024);
		pw1.writeObject(kpg.generateKeyPair());
		pw1.flush();
		pw1.close();

		// certCreate();
		createRSACer();
	}

	public static void test2() throws Exception {
		java.security.KeyPairGenerator kpg = KeyPairGenerator
				.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair keyPair = kpg.generateKeyPair();
		PublicKey keypairpublic1 = keyPair.getPublic();
		PrivateKey keypairprivate1 = keyPair.getPrivate();

		// X509Name
		// X509Name SubjectDN = new X509Name(oids, attributes);

		// KeyFactory kfac = KeyFactory.getInstance("RSA");
		// PublicKey keypairpublic2=kfac.generatePublic(new
		// PKCS8EncodedKeySpec(keypairpublic1.getEncoded()));
		// PrivateKey keypairprivate2 = kfac.generatePrivate(new
		// PKCS8EncodedKeySpec(keypairprivate1.getEncoded()));
		// keyPair=new KeyPair(keypairpublic2,keypairprivate2);
	}

	public static String certCreate() {

		String clientName = "clientName";
		String pkcs12Alias = clientName;
		String KSPASSWORD = "KSPASSWORD";
		String ALIASNAME = "ALIASNAME";
		String pkcs12FileName = "C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/"
				+ pkcs12Alias + ".p12 ";
		String KEYSTOREFILE = "C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/KEYSTOREFILE.keystore";
		char[] keyPassword = (clientName).toCharArray();

		Security.addProvider(new BouncyCastleProvider());

		InputStream jksInputStream = null;
		try {
			if (!new File(KEYSTOREFILE).exists())
				new File(KEYSTOREFILE).createNewFile();
			jksInputStream = new FileInputStream(KEYSTOREFILE);
			System.out.println("Establish   JKS   InputStream   to   "
					+ KEYSTOREFILE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		KeyStore jksKeyStore = null;
		try {
			jksKeyStore = KeyStore.getInstance("JKS", "SUN");
			System.out.println("Create   JKS   KeyStore   Object. ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load the keystore
		try {
			jksKeyStore.load(jksInputStream, KSPASSWORD.toCharArray());
			System.out.println("Load   JKS   KeyStore. ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Take a glance at all aliases from the keystore.
		Enumeration aliases = null;
		try {
			aliases = jksKeyStore.aliases();
			System.out.println("Got   KeyStore   aliases. ");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		// Shows all aliases from the keystore, only for info
		while (aliases.hasMoreElements()) {
			System.out.println("Has   alias:   " + aliases.nextElement());
		}

		// Get PrivateKey
		RSAPrivateCrtKey jksPrivateCrtKey = null;
		try {
			jksPrivateCrtKey = (RSAPrivateCrtKey) jksKeyStore.getKey(ALIASNAME,
					KSPASSWORD.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get Certificate
		Certificate jksCert = null;
		try {
			jksCert = jksKeyStore.getCertificate(ALIASNAME);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		// Get Certificate Chain
		Certificate[] jksCerts = null;
		try {
			jksCerts = jksKeyStore.getCertificateChain(ALIASNAME);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		// =====================================
		// Create PKCS#12
		// =====================================

		KeyStore pkcs12KeyStore = null;
		try {
			pkcs12KeyStore = KeyStore.getInstance("PKCS12 ", "BC ");
			System.out.println("Create   PKCS#12   KeyStore   Object. ");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pkcs12KeyStore.load(null, keyPassword);
			System.out
					.println("Load   a   new   fresh   PKCS#12   KeyStore   from   scratch. ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pkcs12KeyStore.setKeyEntry(pkcs12Alias, jksPrivateCrtKey,
					keyPassword, jksCerts);
			System.out
					.println("Add   the   RSA   Private   Crt   Key   and   the   "
							+ "Certificate   Chain   to   the   PKCS#12   KeyStore. ");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		OutputStream pkcs12OutputStream = null;
		try {
			pkcs12OutputStream = new FileOutputStream(pkcs12FileName);
			System.out.println("Establish   PKCS#12   OutputStream   to   "
					+ pkcs12FileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			pkcs12KeyStore.store(pkcs12OutputStream, keyPassword);
			pkcs12OutputStream.flush();
			pkcs12OutputStream.close();
			System.out.println("Store   PKCS#12   KeyStore:   "
					+ pkcs12FileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// =====================================
		// Reread the pkcs12KeyStore
		// =====================================

		InputStream pkcs12InputStream = null;
		try {
			pkcs12InputStream = new FileInputStream(pkcs12FileName);
			System.out.println("Establish   PKCS#12   InputStream   to   "
					+ pkcs12FileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			pkcs12KeyStore.load(pkcs12InputStream, keyPassword);
			System.out.println("Re-read   the   PKCS#12   KeyStore. ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get PrivateKey
		RSAPrivateCrtKey pkcs12PrivateCrtKey = null;
		try {
			pkcs12PrivateCrtKey = (RSAPrivateCrtKey) pkcs12KeyStore.getKey(
					pkcs12Alias, keyPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get Certificate
		Certificate pkcs12Cert = null;
		try {
			pkcs12Cert = pkcs12KeyStore.getCertificate(pkcs12Alias);
			// System.out.println( "Get   Certificate   from   PKCS#12:   " +
			// pkcs12Cert);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		// Get Certificate Chain
		Certificate[] pkcs12Certs = null;
		try {
			pkcs12Certs = pkcs12KeyStore.getCertificateChain(pkcs12Alias);
			System.out
					.println("Get   Certificate   Chain   from   PKCS#12,   with   "
							+ pkcs12Certs.length + "   certs. ");
		} catch (KeyStoreException e) {
			e.printStackTrace();
			// System.exit(1);
		}
		return pkcs12FileName;
	}

	
	public static void createRSACer() throws Exception{
     //1。首先生成selfcert
     CertAndKeyGen cak = new CertAndKeyGen("RSA","MD5WithRSA",null);
//     参数分别为 公钥算法 签名算法 providername（因为不知道确切的 只好使用null 既使用默认的provider）
     cak.generate(1024);
//     生成一对key 参数为key的长度 对于rsa不能小于512
     X500Name subject = new X500Name("CN=simic,o=shanghai");
//     subject name
     X509Certificate certificate = cak.getSelfCertificate(subject,10);
//      后一个long型参数代表从现在开始的有效期 单位为秒（如果不想从现在开始算 可以在后面改这个域）
     BASE64Encoder base64 = new BASE64Encoder();
     FileOutputStream fos = new FileOutputStream(new File("C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/test.crt"));
     base64.encodeBuffer(certificate.getEncoded(), fos);
//     生成cert文件 base64加密 当然也可以不加密

     //2。生成非自签的cert
     //首先按照1走一遍生成一个自签证书
     byte certbytes[] = certificate.getEncoded();
     X509CertImpl x509certimpl = new X509CertImpl(certbytes);
     X509CertInfo x509certinfo = (X509CertInfo)x509certimpl.get("x509.info");
     X500Name issuer = new X500Name("CN=fatal,o=shanghai");
     x509certinfo.set("issuer.dname",issuer);
     
//     设置issuer域
//     validity为有效时间长度 单位为秒
     long validity=365;
     Date bdate = new Date();
     Date edate = new Date();
     edate.setTime(bdate.getTime() + validity * 1000L * 24L * 60L * 60L);
     CertificateValidity certificatevalidity = new CertificateValidity(bdate, edate);
     x509certinfo.set("validity", certificatevalidity);
//     设置有效期域（包含开始时间和到期时间）域名等同与x509certinfo.VALIDITY
     x509certinfo.set("serialNumber", new CertificateSerialNumber((int)(new Date().getTime() / 1000L)));
//     设置序列号域
     CertificateVersion cv = new CertificateVersion(CertificateVersion.V3);
     x509certinfo.set(X509CertInfo.VERSION,cv);
//     设置版本号 只有v1 ,v2,v3这几个合法值
     
     /**
     *以上是证书的基本信息 如果要添加用户扩展信息 则比较麻烦 首先要确定version必须是v3否则不行 然后按照以下步骤
     **/
//     生成扩展域的id 是个int数组 第1位最大2 第2位最大39 最多可以几位不明....
     ObjectIdentifier oid = new ObjectIdentifier(new int[]{1,22});
//      生成一个extension对象 参数分别为 oid，是否关键扩展，byte[]型的内容值
//     其中内容的格式比较怪异 第一位是flag 这里取4暂时没出错 估计用来说明数据的用处的 第2位是后面的实际数据的长度，然后就是数据
     String userData = "hohohohohahahahah";
     byte[] bs = new byte[userData.length()+2];
     byte l = 0x11;//数据总长17位
     byte f = 0x04;
     bs[0] = f;
     bs[1] = l;
     for(int i=2;i<bs.length;i++)
      {
       bs[i] = (byte)userData.charAt(i-2);
     }
     Extension ext = new Extension(oid,true,bs);

//     如果有多个extension则都放入CertificateExtensions 类中，
     CertificateExtensions exts = new CertificateExtensions();
     exts.set("aa",ext);
//     设置extensions域
     x509certinfo.set(X509CertInfo.EXTENSIONS,exts);

     X509CertImpl x509certimpl1 = new X509CertImpl(x509certinfo);
//     使用另一个证书的私钥来签名此证书 这里使用 md5散列 用rsa来加密
     x509certimpl1.sign(cak.getPrivateKey(), "MD5WithRSA");

     FileOutputStream fos2 = new FileOutputStream(new File("C:/Documents and Settings/zhangyy.BQ-ZHANGYY-6/桌面/test2.crt"));
     base64.encodeBuffer(x509certimpl1.getEncoded(), fos2);
//     生成文件
     x509certimpl1.verify(cak.getPublicKey(),null);
 }
}
