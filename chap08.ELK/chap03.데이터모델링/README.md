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




```

### 메타 필드
> 메타 필드는 엘라스틱서치에서 생성한 문서에서 제공하는 특별한 필드다.  
> 이것은 메타데이터를 저장하는 특수 목적의 필드로서 이를 이용하면  
> 검색 시 다양한 형태로 제어하는 것이 가능해 진다.  


