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

* movieNmEn 필드에 Family가 포함된 모든 문서를 검색하는 예
GET movie_search/_search?q=movieNmEn:Family

GET movie_search/_search
?q=movieNmEn:* AND prdtYear:2017                    1. 필드에 대한 쿼리 
&analyze_wildcard=true                              2. 와일드 카드 옵션 활성화
&from=0                                             3. 페이징을 위한 시작값 설정
&size=5                                             4. 사이즈 설정      
&sort=_score:desc,movieCd:asc                       5. 결과 정렬
&_source_includes=movieCd,movieNm,movieNmEn,typeNm  6. 포함할 _source 필드명
```
| 파라미터 | 기본값 | 설명 |
|---|:---:|---:|
| `q` | - | 검색을 수행할 쿼리 문자열 조건 지정 |
| `df` | - | 쿼리에 검색을 수행할 필드가 지정되지 않을 경우 기본값으로 검색할 필드를 지정. |
| `analyzer` | 검색 대상 필드에 설정된 형태소 분석기 | 쿼리 문자열을 형태소 분석할 때 사용할 형태소 분석기 지정 |
| `analyze_wildcard` | false | 접두어/ 와일드 카드 검색 활성화 여부 |
| `default_operator` | OR | 두개 이상의 검색 조건이 쿼리 문자열에 포함된 경우 검색 조건 연산자 |
| `_source_` | true | 검색 결과에 문서 본문 포함 여부를 지정 |
| `sort` | - | 검색 결과의 정렬 기준 필드를 지정 |
| `from` | - | 검색을 시작할 문서의 위치 |
| `size` | - | 반환할 검색 결과 개수 |


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