# 기초  

## 엘라스틱 서치 설치(우분투 기준16.04)  
```
> wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.4.3.tar.gz  
> tar -xvf 파일명
> cd 파일명
> bin/elasticsearch
> curl -XGET 'http://localhost:9200'
```

## 설정관련 (config/elasticsearch.yml)

1. cluster.name   
    클러스터로 여러 노드를 하나로 묶을 수 있는데 여기서 클러스터명을 지정할 수 있다.
2. node.name  
    노드명을 설정한다. 노드명을 지정하지 않으면 엘라스틱서치가 임의의 이름을 자동으로 부여한다.
3. path.data
    인덱스 경로를 지정한다. 설정하지 않으면 기본적으로 엘라스틱서치 하위의 data디렉터리에 인덱스가 생성된다.
4. path.logs  
    노드와 클러스터에서 생성되는 로그를 저장할 경로를 지정한다. 기본경로는 /path/to/logs
5. path.repo  
    인덱스를 백업하기위한 스냅숏의 경로를 지정한다. 
6. network.host  
    특정 IP만 엘라스틱서치에 접근하도록 허용할 수 있다. 선택적으롤 IP를 허용해야 할 경우 [1.1.1.1, 2.2.2.2]와 같이 지정하고 모든 IP를 허용한다면 0.0.0.0 지정하면 된다. 
7. http.port  
    서버에 접근할 수 있는 http api 호출을 위한 포트번호를 지정한다. 기본값 9200
8. transport.tcp.port  
    클라이언트가 접근할 수 있는 TCP포트이다. 기본값은 9300
9. discovery.zen.ping.unicast.hosts  
    노드가 여러개인 경우 유니캐스트로 활성화된 다른 서버를 찾는다. 클러스터로 묶인 노드의 IP를 지정하면 된다. 예컨대 노드가 2개인 경우 [1.1.1.1, 2.2.2.2]처럼 지정하면 된다.
10. discovery.zen.minimum_master_nodes  
    마스터 노드의 선출기준이 노드의 수를 지정한다. 
11. node.master  
    마스터 노드로 동작여부를 지정한다. 
12. node.data  
    데이터 노드로 동작여부를 지정한다.  
