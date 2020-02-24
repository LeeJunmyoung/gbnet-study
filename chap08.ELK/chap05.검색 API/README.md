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

EX1)
GET movie_search/_search
?q=movieNmEn:* AND prdtYear:2017                    1. 필드에 대한 쿼리 
&analyze_wildcard=true                              2. 와일드 카드 옵션 활성화
&from=0                                             3. 페이징을 위한 시작값 설정
&size=5                                             4. 사이즈 설정      
&sort=_score:desc,movieCd:asc                       5. 결과 정렬
&_source_includes=movieCd,movieNm,movieNmEn,typeNm  6. 포함할 _source 필드명
```
| 파라미터 | 기본값 | 설명 |
|:---:|:---:|---|
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

* movieNmEn 필드에 Family가 포함된 모든 문서를 검색하는 예
POST movie_search/_search
{
    "query" : {
        "query_string" : {
            "default_field" : "movieNmEn"
            , "query" : "Family"
        }
    }
}

EX1)
POST movie_search/_search
{
    "query" : {
        "query_string" : {
            "default_field" : "movieNmEn"
            , "query" : "movieNmEn:* OR prdtYear:2017"
        }
    }
    , "from" : 0
    , "size" : 5
    , "sort" : [
        {
            "_score" : {
                "order" : "desc"
            }
            , "movieCd" : {
                "order" : "asc"
            }
        }
    ]
    , "_source" : [
        "movieCd"
        , "movieNm"
        , "movieNmEn"
        , "typeNm"
    ]
} 
```

#### 쿼리 필터
|구분|쿼리 컨텍스트|필터 컨텍스트|
|:---:|---|---|
|용도|전문검색 시 사용| 조건 검색 시 사용|
|특징|분석이에 의해 분석이 수행<br>  연광성관련score계산|Yes/No 판별<br>  연광성 계산 X|
|ex|돼지고기 김치찌개|Y/N

1. 쿼리 컨텍스트
> 문서가 쿼리와 얼마나 유사한지를 스코어로 계산  
> 질의가 요청될 때마다 엘라스틱서치에서 내부의 루씬을 이용해 계산 수행    
> 일반적으로 전문검색에 많이 사용.  
> 캐싱되지 않고 디스크 연산을 수행하기 때문에 상대적으로 느림.  
```
{
    "query" : {
        "match" : {
            "movieNm" : "가족"
        }
    }
}
```


2. 필터 켄텍스트
> 쿼리의 조건과 문서가 일치자는 구분  
> 별도로 스코어를 계산하지 않고 매칭 여부 검사  
> 자주 사용되는 필터의 결과는 엘라스틱서치가 내부적으로 캐싱  
> 기본적으로 메모리 연산을 수행하기 때문에 상대적으로 빠름.  
```
{
    "query" : {
        "bool" : {
            "must" : [
                {
                    "match_all" : {}
                }
            ],
            "filter" : {
                "term" : {
                    "repGenreNm" : "다큐멘터리"
                }
            }
        }
    }
}
```

#### 쿼리 DSL 파라미터 옵션

1. Multi Index 검색
> 검색 요청시 ","를 이용해 다수의 익덱스를 검색할 수 있다.   
> "*"를 와일드 카드로 사용할 수 있다.  
```
POST movie_search,movie_auto/_search
{
    "query" : {
        "term" : {
            "repGenreNm" : "다큐멘터리"
        }
    }
}

POST log-*/_search
```

2. 페이징
```
# 첫 번째 페이지
POST movie_search/_search
{
    "from" : 0,
    "size" : 5,
    "query" : {
        "term" : {
            "repGenreNm" : "다큐멘터리"
        }
    }
}

# 첫 번째 페이지
POST movie_search/_search
{
    "from" : 5,
    "size" : 5,
    "query" : {
        "term" : {
            "repGenreNm" : "다큐멘터리"
        }
    }
}
```

3. 정렬
```
POST movie_search/_search
{
    "query" : {
        "term" : {
            "repGenreNm" : "다큐멘터리"
        }
    },
    "sort" : {
        "prdtYear" : {
            "order" : "asc"  -- 1번째 정렬조건
        },
        "_score" : {
            "order" : "desc" -- 2번째 정렬조건
        }
    }
}
```

4. _source 필드
> 검색 요청 시 _source에 검색 결과에 포함하고 싶은 필드를 지정  
```
POST movie_search/_search
{
    "_source" : [
        "movieNm" -- movieNm 필드만 출력됨
    ],
    "query" : {
        "term" : {
            "repGenreNm" : "다큐멘터리"
        }
    }
}
```

5. 범위 검색  

|문법|연산자|설명|
|:---:|:---:|---|
| lt | < | 피연산보다 작음 |
| gt | > | 피연산보다 큼 |
| lte | <= | 피연산자보다 작거나 같다 |
| gte | >= | 피연산자보다 크거나 같다 |
```
POST movie_search/_search

{
    "query" : {
        "range" : {
            "prdtYear" : {
                "gte" : "2016",
                "lte" : "2017"
            }
        }
    }
}
```

6. operator
> 엘라스틱 서치는 검색 시 문장이 들어올 경우 기본적으로 OR연산으로 동작.  
> 하지만, AND연산을 사용해 정확도를 높여 검색해야 할 때가 많다.  
```
POST movie_search/_search
{
    "query" : {
        "match" : {
            "movieNm" : {
                "query" : "해리포터 마법",
                "operator" : "and"
            }
        }
    }
}
# operator 파라미터를 생략하면 OR연산을 통해 '해리포터', '마법'이 들어간 모든 단어를 검색  
# "and"값을 명시해 두개의 텀이 모두 존재하는 문서만 결과로 제공.
```

7 minimum_should_match  
> OR 연산을 수행할 경우에 사용할 수 있는 옵션  
> 텀의 개수가 몇 개 이상 매칭 될때만 결과로 나오게 할 수 있음.  
```
{
    "query" : {
        "match" : {
            "movieNm" : {
                "query" : "해리포터 마법",
                "minimum_should_match" : 2
            }
        }
    }
}
```

8. fuzziness
> fuzziness 파라미터를 사용하면 단순히 같은 값을 찾는 match query를 유사한 값을 찾는 fuzzy query로 변경 할 수 있다.  
> 레벤슈타인 편집 거리 알고리즘을 기반으로 문서의 필드 값을 여러번 변경하는 방식.  
> 편집거리의 수를 2로 설정하면 오차범위가 두글자 이하인 검색 결과까지 포함해서 결과로 출력.   
> 오차범위 값으로 0, 1, 2, AUTO로 총 4가지 값을 사용할 수 있음.  
> 알파벳에는 유용하지만 한국어에는 적용하기 어려움.  
```
{
    "query" : {
        "match" : {
            "movieNm" : {
                "query" : "fli high",
                "fuzziness" : 1
            }
        }
    }
}
```

9. boost
> 관련성이 높은 필드나 키워드에 가중치를 더 줄 수 있다.  
```
{
    "query" : {
        "multi_match" : {
            "query" : "fly",
            "fields" : ["movieNm^3", "movieNmEn"]
        }
    }
}
```