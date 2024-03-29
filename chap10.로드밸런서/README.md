# 로드밸런서
> 부하분산 또는 로드 밸런싱은 컴퓨터 네트워크 기술의 일종으로 둘 혹은 셋이상의 중앙처리장치 혹은 저장장치와 같은 컴퓨터 자원들에게 작업을 나누는 것을 의미한다.  

## 네트워크 로드밸런서 종류  
  1. L2(Data Link Layer)  
    - Mac Address Load Balancing  
    - 예시 : Mac > 80–00–20–30–1C-47  
    - 브릿지, 허브 등  
    - 장점 : 구조가 간단, 신뢰성이 높다, 가격저렴, 성능이 좋다.  
    - 단점 : Broadcast 패킷에 의해 성능저하 발생, 라우팅 등 상위레이어 프로토콜 기반 스위칭 불가  

  2. L3(Network Layer)  
    - IP Address Load Balancing  
    - 예시 : IP > 213.12.32.123  
    - L2 + Routing  
    - Router, ICMP 프로토콜, IP  
    - 장점: Broadcast 트래픽으로 전체 성능 저하 방지, 트레픽체크  
    - 단점: 특정 프로토콜을 이용해야 스위칭 가능  

  3. L4(Transport Layer)  
    - Transport Layer(IP+Port) Load Balancing  
    - 예시: IP+Port > 192.168.0.1:80, 123.1.55.444:7979
    - TCP, UDP Protocol  
    - 장점 : Port기반 스위칭 지원, VIP를 이용하여 여러대를 한대로 묶어 부하분산  
  
  4. L7(Application Layer)  
    - Application Layer(사용자 Request) Load Balancing  
    - 예시 : IP+Port+패킷 내용 >  
    192.168.0.1:80, 123.1.55.444:7979 + GET/ img/aaa.jpg
    - HTTP, FTP, SMTP Protocol  

## 1. 로컬 서버 구축
> 빠르게 구축하기위에 npm 에 live-server 설치
> 여러개의 서버를 올리도록하자
```
# live-server 전역 설치
npm install -g live-server

# 여러개의 하위 폴더에
live-server --port=4000
live-server --port=5000
live-server --port=7000
live-server --port=8000

# 구별하기 위해 각 폴더에 index.html 생성
<!DOCTYPE html> <!-- 문서타입 --> 
<html lang="ko"> <!-- 휴먼랭귀지 --> 
	<head> 
		<meta charset="utf-8"> <!-- 문자셋 --> 
		<title>{port}포트입니다.</title> 
	</head> 
	<body> 
		<h1>현재 귀하가 바라보고 있는 포트는 {port}포트입니다.</h1>
	</body> 
</html>
```

## 2. nginx 로드밸런싱 설정

### 2-1 nginx.conf
```
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;
	

    sendfile        on;
    tcp_nopush     on;

    keepalive_timeout  0;

    # 로드밸런싱 될 서버들.
	upstream myserver {
		server localhost:4000;# max_fails=2 fail_timeout=30s;
		server localhost:5000;# max_fails=2 fail_timeout=30s;
		server localhost:7000;# max_fails=2 fail_timeout=30s;
		#server localhost:8000;# max_fails=2 fail_timeout=30s;
	}

    server {
        listen       8080; #nginx 포트번호
		location / {
			proxy_pass http://myserver;  # 프록시 설정.
		}
    }
}

```  

1. 업스트림 옵션  

| 옵션 | 설명 |    
|:---:|:---|  
|weight=n|업스트림 서버의 가중치를 부여한다. 기본 1|  
|max_fails=n|n으로 지정한 횟수만큼 실패가 일어나면 서버가 죽은 것으로 간주.|  
|fail_timeout=n|max_fails가 지정된 상태에서 이값이 설정만큼 서버가 응답하지 않으면 죽은것으로 간주|  
|backup|모든서버가 동작하지 않을때 backup으로 표시된 서버가 사용되고 그전 에는 사용되지 않는다|  
  

2. 업스트림 메소드  

|메소드|설명|   
|:---:|:---|   
|round-robinn|라우든 로빈. (default)|
|ip_hash|아이피를 해싱해서 분배|  
|random|랜덤|  
|least_conn|연결수가 가장 적은 서버를 선택해서 분배, 가중치를 고려함|  
|least_time|연결수가 가장 적으면서 평균 응답시간이 가장 적은 쪽을 선택해서 분배.|  



### 2-2. 브라우저를 새로고침을 통해 로드밸런싱 확인.

## 3. HAProxy  
> L4, L7와 같은 Hardware LoadBalancer를 대체하기 위한 Open Source로 Reverse Proxy를 기반으로한 L4, L7 Software LoadBalancer이다.  


### 3-1. HAProxy 설치 (ubuntu 18)  
```
# 설치
sudo apt-get install haproxy

# config 수정
sudo vi /etc/haproxy/haproxy.cfg

# HAProxy 시작
service haproxy start
```

### 3-2. 설정 수정  
```
global
        log /dev/log    local0
        log /dev/log    local1 notice
		chroot /var/lib/haproxy
        stats socket /run/haproxy/admin.sock mode 660 level admin
        stats timeout 30s
        user root
        group root
        daemon

        # Default SSL material locations
        ca-base /etc/ssl/certs
        crt-base /etc/ssl/private

        # Default ciphers to use on SSL-enabled listening sockets.
        # For more information, see ciphers(1SSL). This list is from:
        #  https://hynek.me/articles/hardening-your-web-servers-ssl-ciphers/
        ssl-default-bind-ciphers ECDH+AESGCM:DH+AESGCM:ECDH+AES256:DH+AES256:ECDH+AES128:DH+AES:ECDH+3DES:DH+3DES:RSA+AESGCM:RSA+AES:RSA+3DES:!aNULL:!MD5:!DSS
        ssl-default-bind-options no-sslv3

defaults
        log     global
        mode    http
        option  httplog
        option  dontlognull
        timeout connect 2000
        timeout client  50000
        timeout server  50000
        errorfile 400 /etc/haproxy/errors/400.http
        errorfile 403 /etc/haproxy/errors/403.http
        errorfile 408 /etc/haproxy/errors/408.http
        errorfile 500 /etc/haproxy/errors/500.http
        errorfile 502 /etc/haproxy/errors/502.http
        errorfile 503 /etc/haproxy/errors/503.http
        errorfile 504 /etc/haproxy/errors/504.http


frontend load-balancer
  bind *:8888
  mode http
  acl my-url hdr(host) -i front.local
  default_backend server

backend server
  mode http
  option forwardfor
  # 헬스체크
  option httpchk GET /
  http-check expect status 200
  default-server inter 1s fall 2 rise 2
  # 로드 밸런싱
  balance roundrobin
  server server-blue 192.168.43.123:9999 check
  server server-green 192.168.43.123:10000 check
  
listen http-in
  bind *:6999 # 관리자페이지?의 아이피:포트
  mode http
  balance
  timeout client 5000
  timeout connect 4000
  timeout server 30000

  #This is the virtual URL to access the stats page
  stats uri /haproxy_stats


  #Authentication realm. This can be set to anything. Escape space characters with a backslash.
  stats realm HAProxy\ Statistics

  #The user/pass you want to use. Change this password!
  # 아이디:패스워드
  stats auth myroot:password

  #This allows you to take down and bring up back end servers.

  #This will produce an error on older versions of HAProxy.
  stats admin if TRUE
```

### 3-3. HAProxy 로깅 활성화
```
# rsyslog 설정 수정
$ sudo vi /etc/rsyslog.conf

# provides UDP syslog reception
module(load="imudp")
input(type="imudp" port="514")

# rsyslog 재시작
service rsyslog restart
```

### 3-4. 브라우저를 새로고침을 통해 로드밸런싱 확인.
