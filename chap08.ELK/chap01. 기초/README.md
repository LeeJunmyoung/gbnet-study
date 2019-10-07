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