# 로드밸런서
> 부하분산 또는 로드 밸런싱은 컴퓨터 네트워크 기술의 일종으로 둘 혹은 셋이상의 중앙처리장치 혹은 저장장치와 같은 컴퓨터 자원들에게 작업을 나누는 것을 의미한다.  

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
|least_time|연결수가 가장 적으면서 평균 응답시간이 가장 적은 쪽을 선택해서 분배|  



### 2-2. 브라우저를 새로고침을 통해 로드밸런싱 확인.