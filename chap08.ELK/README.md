# ELK
> E(Elastic Search) : 분산형 Rest ful 검색 및 분석 엔진.   
> L(Logstash) : 서버측 데이터 처리 파이프라인.(INPUT)
> K(Kibana) : 데이터를 시각적으로 탐색하고 실시간으로 분석.

## Elastic Search
```
# 설치
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.3.1.deb
dpkg -i elasticsearch-5.1.1.deb

# 서비스 시작
service elasticsearch start

# 작동여부 확인
curl -XGET 'localhost:9200'
```

Elastic Search|R DB
-----|-----
Index|DataBase
Type|Table
Document|Row(Record)
Field|Column
Mapping|Schema
  
Elastic Search|R DB|CRUD
-----|-----|-----
GET|SELECT|READ
PUT|UPDATE|UPDATE
POST|INSERT|CREATE
DELETE|DELETE|DELETE