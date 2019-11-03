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
```
