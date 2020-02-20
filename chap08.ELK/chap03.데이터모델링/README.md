# 데이터 모델링
> 엘라스틱 서치에서는 색인할 때 문서의 데이터 유형에 따라 필드에 적절한 데이터 타입을 지정  
> 매핑은 색인될 문서의 데이터 모델링이라고도 할수 있음.  
> 사전에 매핑을 설정하면 지정된 데이터 타입으로 색인되지만 매핑을 설정해두지 않으면 엘라스틱 서치가 자동으로 필드를 생성하고 플드 타입까지 결정  
> 매핑은 사람이 직접 !   
> 한번 생성된 매핑의 타입은 변경 불가. 타입을 변경하려면 인덱스를 삭제한 후 다시 생성하거나 매핑을 다시 정의해야 한다.  

## 매핑 인덱스 만들기

### 예시

#### 영화 정보 문서의 구조
매핑명|필드명|필드타입
-----|-----|-----
인덱스 키|movieCd|keyword
영화제목_국문|movieNm|text
영화제목_영문|movieNmEn|text
제작년도|prdtYear|integer
개봉년도|openDt|integer
영화유형|typeNm|keyword
제작상태|prdtStatNm|keyword
제작국가(전체)|nationAlt|keyword
장르(전체)|genreAlt|keyword
대표제작국가|repNationNm|keyword
대표장르|repGenreNm|keyword
영화감독명|directors.peopleNm|object->keyword
제작사코드|companies.companyCd|object->keyword
제작사명|companies.companyNm|object->keyword

### 색인할 데이터 
``` 
{
	"movieCd"       : "20173732",
	"movieNm"       : "착한 아이",
	"movieNeEn"     : "Be Good Child",
	"prdtYear"      : "2017",
	"openDt"        : "",
	"typeNm"        : "장편",
	"prdtStatNm"    : "기타",
	"nationAlt"     : "한국",
	"genreAlt"      : "드라마,가족",
	"repNationNm"   : "한국",
	"repGenreNm"    : "드라마",
    "directors"     :[{
        "peopleNm": "아무개"
    }],
    "companies"     : [
        "companyCd" : "",
        "companyNm" : ""
    ]
}
```
> 실제 검색 대상이 되는 필드는 '영화제목' 필드이므로 분석가능하도록 text타입으로 정의  
> 나머지 필드는 해당 정보를 그대로 보여주기만 할 것이기 때문에 특성에 따라 integer, keyword타입으로 설정.  
> 감독 정보를 나타내는 directors 필드와 제작사 정보를 나타내는 companies 필드는 내부적으로 또 다른 문서 구조를 가지게 되므로 이를 고려해서 계층 구조로 설정

### 매핑
```
> PUT movie_search

{
    "setting": {
        "number_of_shards" : 5,
        "number_of_replicas" : 1  
    },
    "mappings" : {
        "_doc" : {
            "properties" : {
                "movieCd" : {
                    "type" : "keyword"
                },
                "movieNm" : {
                    "type" : "text"
                    , "analyzer" : "standard"
                },
                "movieNmEn" : {
                    "type" : "text"
                    , "analyzer" : "standard"
                },
                "prdtYear" : {
                    "type" : "integer"
                },
                "openDt" : {
                    "type" : "integer"
                },
                "typeNm" : {
                    "type" : "keyword"
                },
                "prdtStatNm" : {
                    "type" : "keyword"
                },
                "nationAlt" : {
                    "type" : "keyword"
                },
                "genreAlt" : {
                    "type" : "keyword"
                },
                "repNationNm" : {
                    "type" : "keyword"
                },
                "repGenreNm" : {
                    "type" : "keyword"
                },
                "companies" : {
                    "properties" : {
                        "companyCd" : {
                            "type" : "keyword"
                        },
                        "companyNm" : {
                            "type" : "keyword"
                        }
                    }
                },
                "directors" : {
                    "properties" : {
                        "peopleNm" : {
                            "type" : "keyword"
                        }
                    }
                }
            }
        }
    }
}
```

### 매핑확인
```
> GET localhost:9200/movie_search/_mapping

> 매핑 파라미터

1. analzer  
- 해당 필드의 데이터를 형태소 분석하겠다는 의미의 파라미터다. 색인과 검색 시 지정한 분석기로 형태소 분석을 수행.  
tex데이터 타입의 필드는 analyzer 매핑 파라미터를 기본적으로 사용해야 한다. 별도의 분석기를 지정하지 않으면   
Standard Analyzer로 형태소 분석을 수행.  

2. normalizer  
- normalizer 매핑 파라미터는 term query에 분석기를 사용하기 위해 사용. 예를 들어 keyword데이터 타입의 경우  
원문을 기준으로 문서가 색인되기 때문에 cafe, Cafe, Cafe` 는 서로 다른 문서로 인식됨.   
하지만 해당 유형을 normalizer를 통해 분석기에 asiifolding과 같은 필터를 사용하면 같은 데이터로 인식됨.

3. boost  
- 필드에 가중치를 부여함. 가중치에 따라 유사도(_score)가 달라지기 때문에 boost설정 시 검색 결과의 노출 순서에 영향을 줌.  
만약 색인 시점에 boost 설정을 하게 된다면 재색인 하지 않는 이상 가중치 변경을 할 수 없기 때문에 주의해서 사용해야 함.  
가급적이면 검색 시점에만 사용하는 것을 권장.  
* 최근 엘라스틱서치는 색인 시 boost 설정을 할 수 없도록 바뀜. 내부적으로 사용하는 루씬에서 기능이 제거 됌.  
루씬 7.0 버전부터 색인시 boost설정 기능이 제거 됨.

4. coerce  
- 색인 시 자동 변환을 허용할지 여부를 설정하는 파라미터.  
예를 들어 "10"과 같은 숫자 형태의 문자열이 integer 타입의 필드에 들어온다면  
엘라스틱서치는 자동으로 형변환을 수행해서 정상적으로 처림함.  
하지만 coerce 설정을 미사용으로 변경한다면 색인에 실패할 것.  

5. copy_to  
- 매핑 파라미터를 추가한 필드의 값을 지정한 필드로 복사함.  
예컨대 keyword타입의 필드에 copy_to 매핑 파라미터를 사용해  
다른 필드로 값을 복사하면 복사된 필드에서는 text 타입을 지정해 형태소 분석.
또한 여러개의 필드 데이터를 하나의 필드에 모아서 전체 검색 용도로 사용하기도 함.  
이를 통해 과거에 존재하던 _all 칼럼과 동일한 기능을 제공함.

6. fielddata
- 엘라스틱 서치가 힙 공간에 생성하는 메모리 캐시다. 최신 버전의 엘라스틱 서치는 doc_values라는 새로운  
형태의캐시를 제공하며, text타입의 필드를 제외한 모든 필드는 기본적으로 doc_values캐시를 사용함.
flelddata를 사용해야하는 경우는 text타입의 필드는 기본적으로 분석기에 의해 형태소 분석이  
되기 때문에 집계나 정렬 등의 기능을 수행할 수 없다. 하지만 부득이하게 text타입의 필드에서 집계나  
정렬을 수행하는 경우도 있을 것이다. 
* 사용법
> PUT movie_search_mapping/_mapping/_doc

> Request Body
{
    "properties" : {
        "nationAltEn" : {
            type: "text"
            , "fielddata": true
        }
    }
}

7. doc_values
- 엘라스틱서치에서 사용하는 기본 캐시. text타입을 제외한 모든 타입에서 기본적으로 doc_values캐시를 사용함.  
doc_values는 루씬을 기반으로 하는 캐시 방식. 과거에는 캐시를 모두 메모리에 올려 사용했으나  
현재는 doc_values를 사용함으로써 힙 사용에 대한 부담을 없애고 운영체제의 파일 시스템 캐시를 통해 디스크에 있는  
데이터에 빠르게 접근 할 수 있다.

8. dynamic  
- 매핑에 필드를 추가할때 동적으로 생성할지, 생성하지 않을지를 결정함.  
true - 새로 추가되는 필드를 매핑에 추가한다.  
false - 새로 추가되는 필드를 무시한다. 해당 필드는 색인되지 않아 검색할 수 없지만 _source에는 표시된다.  
strict - 새로운 필드가 감지되면 옝외가 발생하고 문서자체가 색인되지 않는다.   
새로 유입되는 필드는 사용자가 매핑에 명시적으로 추가해야한다  

9. enabled
- 검색 결과에는 포함하지만 색인은 하고 싶지 않은 경우도 있다. 메타 성격의 데이터  
예컨대 일반적인 게시판이라면 제목과 요약글만 색인하고 날짜와 사용자 ID는 색인하지 않는 경우다.  
색인을 원치 않는 날짜와 사용자 ID의 매핑 파라미터중 enabled를 false로 설정하면 _source에는 검색이 되지만색인은 하지 않는다.

10. format 
- 엘라스틱서치는 날짜/시간을 문자열로 표시. 날짜/시간을 문자열로 변경할 때 미리 구성한 포멧으로 사용 가능.  

11. ignore_above
- 필드에 저장되는 문자열이 지정한 크기를 넘어서면 빈 값으로 색인하다.  
지정한 크기만큼만 색인되는 것이 아니라 빈값으로 저장하므로 주의해야한다.

12. ignore_malformed
- 잘못된 데이터 타입을 색인하려고 하면 예외가 발생하고 해당문서전체가 색인되지 않는다.  
이 매핑 파라미터를 사용하면 해당 필드만 무시하고 문서는 색인 가능.  

13. index
- 필드값을 색인할지를 결정. 기본값 true

14. field
- 다중 필드를 설정할 수 있는 옵션. 필드안에 또다른 필드의 정보를 추가 할 수 있어 같은 string값을 각각 다른 분석기로
처리하도록 설정 가능. 

15. norms
- 문서의 _score 값 계산에 필요한 정규화 인수를 사용할지 여부를 설정. 기본값은 true.

16. null_value
- 문서에 필드가 없거나 필드의 값이 null이면 색인시 필드를 생성하지 않음. 이경우 null_value를 설정하면
문서의 값이 null이더라도 필드를 생성하고 그에 해당하는 값으로 저장.

17. position_increment_gap
- 배열 형태의 데이터를 색인할 때 검색의 정확도를 높이기 위해 제공하는 옵션.

18. properties
- 오브젝트 타입이나 중첩 타입의 스키마를 정희할때 사용하는 옵션.  
필드의 타입을 매핑. 오브젝트 필드 및 중첩 필드에는 properties 라는 서브 필드가 있다.

19. search_analyzer
- 일반적으로 색인과 검색 시 같은 분석기를 사용한다.   
만약 다른 분석기를 사용하고 싶은 경우 search_analyzer를 설정해서 검색 시 사용할 분석기를 별도로 지정할수 있음.

20. similarity
- 유사도 측정 알고리즘을 지정.

21. store
- 필드의 값을 저장해 검색 결과에 값을 포함하기 위한 매핑 파라미터  
기본적으로 _source에 색인된 문서가 저장. 하지만 store매핑 파라미터를 사용하면
해당 필드를 자체적으로 저장할 수 있다.

22. term_vector
- 루씬에서 분석된 용어의 정보를 포함할지 여부를 결정하는 매핑 파라미터.  


```

### 메타 필드
> 메타 필드는 엘라스틱서치에서 생성한 문서에서 제공하는 특별한 필드다.  
> 이것은 메타데이터를 저장하는 특수 목적의 필드로서 이를 이용하면  
> 검색 시 다양한 형태로 제어하는 것이 가능해 진다.  

```
1. _index
- 해당 문서가 속한 인덱스의 이름을 담고 있다. 검색된 문서의 인덱스 명을 알 수 있으며
해당 인덱스에 몇개의 문서가 있는지 확인 할 수 있다.

2. _type
- 해당 문서의 속한 매핑의 타입 정보를 담고 있다. 타입별로 몇개의 문서가 있는지 확인 할 수 있다.

3. _id
- 문서를 식별하는 유일한 키값. 한 인덱스에서 색인된 문서마다 서로 다른 키 값을 가짐.

4. _uid
- # 태그를 사용해 _type와 _id값을 조합해 사용함.  
하지만 내부적으로 사용되기 때문에 검색시 조회되는 값은 아님.

5. _source
- 문서의 원본데이터를 제공. 내부에는 색인 시 전달된 원본 JSON문서의 본문이 저장되어 있다.  

6. _all
- 모든 필드에 내용이 하나의 텍스트로 합쳐져서 제공.  
특정 필드가 아닌 전체 필드에서 특정 키워드를 검색한다면 _all메타 필드를 사용하면 됨.
하지만 _all메타 필드는 데이터 크기를 너무 많이 차지하는 문제가 있어 엘라스틱서치 6.0 이상부터는 폐기 됐다.   
그래서 필드 복사가 필요한 경우 copy_to파라미터를 사용해야 함. copy_to를 이용하면 all과 동일한 효과를 낼 수 있다.

7. _routing
- 특정 샤드에 저장하기 위해 사용자가 지정하는 메타 필드.  
기본적으로 색인을 하면 해당 문서는 다음 수식에 따라 문서 id를 이용해 문서가 색인될 샤드를 결정.  
별도의 설정없이 문서를 색인하면 문서는 샤드에 골고루 분산되어 저장.
```

### 필드 데이터 타입  
> 필드에는 다음과 타입을 지정할 수 있다.  
> keyword, text 같은 문자열 데이터 타입.  
> date, long, double, integer, boolean, ip 같은 일반적인 데이터 타입.  
> 객체 또는 중첩문과 같은 JSON 계층의 특성의 데이터 타입.  
> geo_point, geo_shape 같은 특수한 데이터 타입.  

```
1. Keyword 데이터 타입 
- keyword 데이터 타입은 키워트 형태로 사용할 데이터에 적합한 데이터 타입이다.  
keyword 타입을 사용할 경우 별도의 분석기를 거치지 않고 원문 그대로 색인하기 때문에 특정 코드나 키워드 등 정형화된 콘텐츠에 사용됨.
엘라스틱 서치 일부기능은 형태소 분석을 하지 않아야만 사용이 가능한데 이경우에도 사용됨.
> 검색시 필터링 되는 항목
> 정렬이 필요한 항목
> 집계해야 하는 항목
    1. boost : 필드의 가중치
    2. doc_values : 필드를 메모리에 로드해 캐시로 사용.
    3. index : 해당 필드를 검색에 사용할지 설정.
    4. null_value : 데이터 값이 없는 경우 null로 필드의 값을 대체할지 설정.
    5. store : 필드 값을 별도로 _source에 저장하고 검색 가능하게 할지 설정.

2. Text 데이터 타입
- Text 데이터 타입을 이용하면 색인 시 지정된 분석기가 컬럼의 데이터를 문자열 데이터로 인식하고 분석.
기본적으로 Standard Analyzer를 사용한다. 영화의 제목이나 영화의 설명글과 같이 문장 형태의 데이터에 사용하기 적합.
필드에 검색뿐아니라 정렬이나 집계 연산을 사용해야할 때 멀티필드로 설정.
    1. analyzer : 인덱스와 검색에 사용할 분석기를 선택
    2. boost : 필드의 가중치
    3. fielddata : 정렬, 집계, 스크립트 등에서 메모리에 저장된 필드 데이터를 사용할지 설정.
    4. index : 해당 필드를 검색에 사용할지를 설정
    5. norms : 유사도 점수를 산정할 때 필드 길이를 고려할지를 결정.
    6. store : 필드 값을 필드와 별도로 _source에 저장하고 검색 가능하게 할지를 설정.
    7. search_analyzer : 검색에 사용할 형태소 분석기를 선택.
    8. similarity : 유사도 점수를 구하는 알고리즘을 선택.
    9. term_vecor : Analyzed필드에 텀 벡터를 저장할지를 결정

3. Array 데이터 타입
- 언어의 값으로 영어와 한국어라는 두 개의 데이터를 입력하고 싶을 경우 array데이터 타입을 사용해야 한다.
    1. 문자열 배열["one","two"]
    2. 정수 배열[1,2]
    3. 객체 배열[{"name" : "Jun", "age" : 2o},{"name" : "Jack", "age" : 30}]

4. Numeric 데이터 타입  
- 숫자 데이터 타입은 여러가지 종류가 제공된다.  
    1. long : 최솟값과 최대값을 가지는 부호 있는 64비트 정수.    
    2. integer : 최솟값과 최대값을 가지는 부호 있는 32비트 정수.  
    3. short : 최솟값과 최대값을 가지는 부호 있는 16비트 정수.  
    4. byte : 최솟값과 최대값을 가지는 부호 있는 8비트 정수.  
    5. double : 64비트 부동 소수점을 갖는 수  
    6. float : 32비트 부동 소수점을 갖는 수  
    7. half_float : 16비트 부동 소수점을 갖는 수  

5. Date 데이터 타입  
- Date 타입은 JSON 포맷에서 문자열로 처리.

6. Range 데이터 타입
- 범위가 있는 데이터를 저장할때 사용하는 데이터 타입.
    1. integer_range : 최솟값 최댓값을 갖는 부호 있는 32비트 정수 범위
    2. float_range : 부동 소수점 값을 갖는 32비트 실수 범위
    3. long_range : 최솟값과 최댓값을 갖는 부호 있는 64비트 정수의 범위
    4. double_range : 부동 소수점 값을 갖는 64비트 실수 범위
    5. date_range : 64비트 정수 형태의 밀리초로 표시되는 날짜값의 범위
    6. ip_range : ipv4, ipv6주소를 지원하는 ip값

7. Boolean 데이터 타입
- 참 거짓이라는 두 논리값을 가지는 데이터 타입.

8. geo-point 데이터 타입
- 위도, 경도 등 위치 정보를 담은 데이터를 저장할때 사용.

9 IP데이터 타입
- ip주소와 같은 데이터를 저장하는데 사용.

10 Object데이터 타입
- Json 포맷의 문서는 내부 객체를 계층적으로 포함 할수 있다. 

11. Nested 데이터 타입
- Object 객체 배열을 독립적으로 색인하고 질의하는 형태의 데이터 타입. 
```

### 엘라스틱서치 분석기
> 루씬을 기반으로 구축된 텍스트 기반 검색엔진. 루씬은 내부턱으로 다양한 분석기를 제공  
> '우리나라가 좋은나라 대한민국 화이팅' 토큰화 '우리나라가','좋은나라','대한민국','화이팅'  
> 와 같은 토큰화 한다.  

#### 분석기 구조
> 1. 문장의 특정한 규칙에 의해 수정한다.  
> 2. 수정한 문장을 개별 토큰으로 분리한다.  
> 3. 개별 토큰을 특정한 규칙에 의해 변경한다.  

1. CHARACTER FILTER
> 문장을 분석하기 전에 입력 테스트에 대해 특정한 단어를 변경하거나  
> HTML과 같은 태그를 제거하는 역할을 하는 필터다.  
> 해당 내용은테스트를 개별 토근화하기 전에 전처리 과정이며, ReplaceAll() 함수처럼 패턴으로 텍스트를 변경하거나  
> 사용자가 정의한 필터를 적용할 수 있다.

2. TOKENIZER FILTER
> 분석기를 구성할 때 하나만 사용할 수 있으며 텍스트를 어떻게 나눌 것인지 정의한다.   
> 한글을 분해할 땐 한글 형태소 분석기의 Tokenizer를 사용하고, 영문을 분석할 경우 영문형태소의 분석기를 사용하는등   
> 상황에 맞게 사용한다.

3. TOKEN FILTER
> 토근화된 단어를 하나씩 필터링해서 사용자가 원하는 토큰으로 변환한다. 불필요한 단어를 제거하거나 동의어 사전을 만들어  
> 단어를 추가하거나 영문 단어를 소문자로 변환하는 등의 작업을 수행  
> Token Filter는 여러단계가 순차적으로 이뤄지며 순서를 어떻게 지정하느냐에 따라 검색의 질이 달라질 수 있음.   

```
PUT /movie_analyzer

{
	"settings": {
		"index": {
			"number_of_shards": 5
			, "number_of_replicas": 1
		}
	},
	"analysis": {
		"custom_movie_analyzer": {
			"type": "custom"
			, "char_filter": [ 
				"html_strip"
			]
			, "tokenizer": "standard"
			, "filter": [
				"lowercase"
			]
		}
	}
}

[char_filter] html_strip
전체 텍스트 문장에서 HTML 태그를 제거한다

[tokenizer] standard
Tokenizer Filter를 정의
특수문자 혹은 공백을 기준으로 텍스트를 분할

[filter] lowercase
Token Filter를 정의
모든 토큰을 소문자로 변환.

<B>Elasticsearch</B> is cool
-> Character Filter : html_strip에 의해 html 제거
Elasticsearch is cool
-> Tokenizer : standard 토크나이저로 term 분리
Elasticsearch -> Token 1, Position 1
is -> Token 2, Position 2
cool -> Token 3, Position 3
-> Token Filter : lowercase 필터로 소문자 처리
elasticsearch -> Token 1, Position 1
is -> Token 2, Position 2
cool -> Token 3, Position 3
```