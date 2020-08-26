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
byte[] encrypt = OpenSSL.encrypt("des3", "1234".toCharArray(), "this is the plain text".getBytes(), true);
String s2 = new String(encrypt);
System.out.println(Arrays.toString(encrypt));
System.out.println(s2);

byte[] decrypt = OpenSSL.decrypt("des3", "1234".toCharArray(), encrypt);
String decryptStr = new String(decrypt, "UTF-8");
System.out.println(decryptStr);
```