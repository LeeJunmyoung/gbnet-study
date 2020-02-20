# 검색 API
> 검색 대상이 되는 필드는 분석이 되는 text타입의 유형이 될 수도 있고 분석이 되지 않는 keyword타입의 유형이 될수 있다.  
> 엘라스틱서치에서 다양한 검색 조건을 충족시키기 위해 Query DSL이라는 특수한 쿼리 문법을 제공.  

## 검색 API
> 엘라스틱서치는 색인 시점에 Analyzer를 통해 분석된 Term, 출현빈도, 문서번호와 같이 역색인 구조로 만들어 내부적으로 저장.  
> 검색 시점에는 keyword 타입과 같은 분석이 불가능한 데이터와 Text타입과 같은 분석이 가능한 데이터를 구분해서  
> 분석이 가능할 경우 분석기를 이용해 분석을 수행. 

### 검색 질의 표현 방식
> URI검색  
> Request Body 검색

#### URI검색
```
GET movie_search/_search?q=prdtYear:2018
```
  
#### Request Body 검색
```
POST movie_search/_search

{
    "query" : {
        "term" : {
            "prdtYear" : "2018"
        }
    }
}
```