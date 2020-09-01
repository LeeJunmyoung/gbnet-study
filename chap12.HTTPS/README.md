# HTTP vs HTTPS

## HTTP
> HTTP(HyperText Transfer Protocol)는 W3 상에서 정보를 주고받을 수 있는 프로토콜이다.   
> 주로 HTML 문서를 주고받는 데에 쓰인다. 주로 TCP를 사용하고 HTTP/3 부터는 UDP를 사용한다.  

## HTTPS
> HTTPS(HyperText Transfer Protocol over Secure Socket Layer)는 HTTP의 보안이 강화된 버전이다.   
> HTTPS는 통신의 인증과 암호화를 위해 넷스케이프 커뮤니케이션즈 코퍼레이션이 개발했으며, 전자 로그인에서 널리 쓰인다.  
> HTTPS는 소켓 통신에서 일반 텍스트를 이용하는 대신에, SSL이나 TLS 프로토콜을 통해 세션 데이터를 암호화한다.  
> HTTP 에 TLS가 추가된 개념이라고 보면된다.  

# SSL 과 TLS
> 전송 계층 보안(영어: Transport Layer Security, TLS)    
> 과거 명칭: 보안 소켓 레이어/Secure Sockets Layer, SSL)  
> 컴퓨터 네트워크에 통신 보안을 제공하기 위해 설계된 암호 규약이다.   
> 인터넷 같이 TCP/IP 네트워크를 사용하는 통신에 적용되며, 통신 과정에서 전송계층 종단간 보안과 데이터 무결성을 확보해준다.   

## HISTORY

### SSL
> SSL 규약은 처음에 넷스케이프가 만들었다.  

1. SSL 1.0  
1.0 버전은 공개 된 적이 없다  
2. SSL 2.0  
1995년 2월에 릴리스, 그러나 이 버전은 많은 보안 결함 존재해 3.0 버전으로 곧바로 이어진다. 
3. SSL 3.0  
1996년 릴리스. 결국 3.0 버전은 TLS 버전 1.0의 기초가 된다.  

### TLS
>  SSL(Secure Sockets Layer)에 기반한 기술로, 국제 인터넷 표준화 기구(IETF)에서 표준으로 인정받은 프로토콜이다  

1. TLS 1.0  
1999년도에 SSL 3.0의 업그레이드 버전으로 공개되었다.   
SSL 3.0과 큰 차이가 있는 것은 아니나, SSL 3.0이 가지고 있는 대부분의 취약점이 해결되었다.  
2. TLS 1.2  
2006년 4월에 공개되었다.  
암호 블록 체인 공격에 대한 방어와 IANA 등록 파라메터의 지원이 추가되었다.  
3. TLS 1.3  
2018년 8월 10일 RFC 8446으로 게시되었다.  
서버에서 인증서를 암호화하여 전달하도록 개선되었고, 최초 연결시에 암호화 통신을 개시하는 절차를 간소화하여 성능을 향상하였다.   
오래된 암호화 기술 등을 폐기하였다.  

## 암호화 

### 대칭키 암호(symmetric-key algorithm)
> 암호화와 복호화에 같은 암호 키를 쓰는 알고리즘을 의미한다.  
> 대칭 키 암호에서는 암호화를 하는 측과 복호화를 하는 측이 같은 암호 키를 공유해야 한다.    
> 이러한 점은 공개 키 암호에서 공개 키와 비밀 키를 별도로 가지는 것과 구별된다.   
> 대신, 대부분의 대칭 키 암호는 공개 키 암호와 비교하여 계산 속도가 빠르다는 장점을 가진다.     
> 따라서, 많은 암호화 통신에서는 비밀 키 암호를 사용하여 대칭 키 암호의 공통 키를 공유하고, 그 키를 기반으로 실제 통신을 암호화하는 구조를 사용한다.    

<br>

- openssl
```
# plaintext.txt 파일을 ciphertext.txt 암호화
# -a option is ascii
echo 'this is the plain text' > plaintext.txt;
openssl enc -e -des3 -a -salt -in plaintext.txt -out ciphertext.txt;

enter des-ede3-cbc encryption password: #{암호키}
Verifying - enter des-ede3-cbc encryption password: #{암호키}

# ciphertext.txt를 plaintext2.txt 복화화
openssl enc -d -des3 -a -in ciphertext.txt -out plaintext2.txt;
enter des-ede3-cbc decryption password: #{암호키}
```

- java
```
import org.apache.commons.ssl.OpenSSL;

public class OpenSSLTest {
	public static void main(String args[]) throws Exception {
		
		
		byte[] encrypt = OpenSSL.encrypt("des3", "1234".toCharArray(), "this is the plain text".getBytes(), true);
		String s2 = new String(encrypt);
		System.out.println(s2);
		
		byte[] decrypt = OpenSSL.decrypt("des3", "1234".toCharArray(), encrypt);
		String decryptStr = new String(decrypt, "UTF-8");
		System.out.println(decryptStr);
	}
}

```

### 공개키(public-key cryptography)  
> 암호화와 복호화에 같은 키를 사용하는 대칭키 암호화 방식과 달리, 암호화와 복호화에 사용하는 키가 서로 다른 암호화 방식을 의미한다.  

#### 공개키 전달 방식
> A가 자신만 알고 있는 기밀을 B 에게 전달하고자 할 때 사용한다. B 를 제외한 타인은 이 내용을 알 수 없어야 한다.  
> SSL/TLS에서 두 당사자가 사용할 '대칭키'를 전달하는 용도로 사용
1. B 가 자신의 공개키를 공개
2. A 는 공개키로 문서를 암호화
3. 암호화된 문서를 B 에게 전달
4. B 는 개인키로 이 문서를 복호화

- openssl
```
# RSA 방식의 1024bit 의 개인키를 생성한다.
openssl genrsa -out private.pem 1024;

# private key로 public key를 생성
openssl rsa -in private.pem -out public.pem -outform PEM -pubout

# 암호화 할 데이터
echo 'this is plain text' > file.txt

# 공개키로 암호화
openssl rsautl -encrypt -inkey public.pem -pubin -in file.txt -out file.ssl

# 개인키로 복호화
openssl rsautl -decrypt -inkey private.pem -in file.ssl -out decrypted.txt
```

- java
```
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

public class UtilRSA {

	private Key publicKey;

	private Key privateKey;

	public String decode(String txt) throws Exception {
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(c.doFinal(Base64.getDecoder().decode(txt.getBytes("UTF-8"))));

	}

	public String encode(String txt) throws Exception {
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);

		return new String(Base64.getEncoder().encode(c.doFinal(txt.getBytes("UTF-8"))));
	}

	public void generatorKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.genKeyPair();

		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	public void getPublicKey() throws Exception {
		File f = new File("C:\\Users\\user\\Desktop\\public.pem");
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		String temp = new String(keyBytes);
		String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("-----END PUBLIC KEY-----", "");

		org.apache.commons.ssl.Base64 b64 = new org.apache.commons.ssl.Base64();
		byte[] decoded = b64.decode(publicKeyPEM);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		publicKey = kf.generatePublic(spec);
	}

	public void getPrivateKey() throws Exception {
		String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\user\\Desktop\\private.pem")));
		content = content.replaceAll("\\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "")
				.replace("-----END RSA PRIVATE KEY-----", "");
		byte[] bytes = Base64.getDecoder().decode(content);

		DerInputStream derReader = new DerInputStream(bytes);
		DerValue[] seq = derReader.getSequence(0);

		BigInteger modulus = seq[1].getBigInteger();
		BigInteger publicExp = seq[2].getBigInteger();
		BigInteger privateExp = seq[3].getBigInteger();
		BigInteger prime1 = seq[4].getBigInteger();
		BigInteger prime2 = seq[5].getBigInteger();
		BigInteger exp1 = seq[6].getBigInteger();
		BigInteger exp2 = seq[7].getBigInteger();
		BigInteger crtCoef = seq[8].getBigInteger();

		RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1,
				exp2, crtCoef);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		privateKey = keyFactory.generatePrivate(keySpec);
	}

	public static void main(String[] args) throws Exception {
		String text = "This is the plain text";

		UtilRSA rsa = new UtilRSA();
		rsa.generatorKey();

		String encodeText = rsa.encode(text);
		String decodeText = rsa.decode(encodeText);
		System.out.println("public key : " + Base64.getEncoder().encodeToString(rsa.publicKey.getEncoded()));
		System.out.println("private key : " + Base64.getEncoder().encodeToString(rsa.privateKey.getEncoded()));
		System.out.println("encode : " + encodeText);
		System.out.println("decode : " + decodeText);
		
		System.out.println("====================================================");

		UtilRSA rsa2 = new UtilRSA();
		rsa2.getPublicKey();
		rsa2.getPrivateKey();

		String encodeText2 = rsa2.encode(text);
		String decodeText2 = rsa2.decode(encodeText2);
		System.out.println("public key : " + Base64.getEncoder().encodeToString(rsa2.publicKey.getEncoded()));
		System.out.println("private key : " + Base64.getEncoder().encodeToString(rsa2.privateKey.getEncoded()));
		System.out.println("encode : " + encodeText2);
		System.out.println("decode : " + decodeText2);
	}
}
```

## CA