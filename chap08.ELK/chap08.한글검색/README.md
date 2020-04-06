# 한글검색
> 엘라스틱 서치는 기본적으로 제공하는 Suggest API 를 지원하지만, 한글 키워드를 대상으로는 정상 작동 하지 않는다.  
> 한글 키워드를 지원하려면 직접 자동완성을 구현해야 한다.  

## Suggest API

### Term Suggest API
> 편집거리를 사용해 비슷한 단어를 제안.  
> 편집거리 척도란 어떤 문자열이 다른 문자열과 얼마나 비슷한가를 편집거리를 사용해 알아볼 수 있으며, 두 문자열 사이의 편집거리는 하나의 문자열을 다른 문자열로 바꾸는데 필요한 편집 횟수를 말한다.  
> 엘라스틱 편집거리 계산은 리벤슈타인 편집거리 측정 방식과 윙클러 편집거리 측정 방식을 기본으로 사용.  

```
# love가 포함된 비슷한 데이터를 생성
PUT /movie_term_suggest/_doc/1
{
    "movieNm" :"lover"
}
PUT /movie_term_suggest/_doc/2
{
    "movieNm" :"Fall in love"
}
PUT /movie_term_suggest/_doc/3
{
    "movieNm" :"lovly"
}

# 검색어로 lave를입력
POST /movie_term_suggest/_search
{
    "suggest" : {
        "spell-suggestion" : {
            "text" : "lave",
            "term" : {
                "field" : "movieNm"
            }
        }
    }
}

> lave와 일치파는 텀이 존재하지 않음. 하지만 Term Suggest 결과로 lave와 가장 유사한 단어를 추출해서 제공함.  
> text는 제안한 문서를 나타냄.  score는 제안하고자 하는 텍스트와 원본이 얼마나 가까운지 나타냄.  freq는 전체 문서에서 해당 텀의 빈도가 얼마나 나오는지 보여줌.
```

1. Term Suggest API 한글 처리
> 한글의 자소를 분해새서 문서를 처리한 후 색인할 경우 영문과 동일하게 추천기능을 구현하는 것이 가능.
> 한글의 경우 ICU분석기를 통해
> https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-icu.html#analysis-icu-install

### Completion Suggest API
> 엘라스틱서치는 자동완성을 위해 Completion Suggest API를 제공함.  
> 자동완성은 글자가 입력 될때마다 검색 결과를 보여줘야 하기 때문에 Term Suggest API 와는 다르게 응답속도가 매우 중요함.  
> 그래서 FST(Finite State Transducer)를 사용함. FST는 검색어가 모두 메모리에 로드되어 서비스 되는 구조
> 자동 완성 기능을 사용하기 위해서는 데이터 타입을 completion으로 설정해서 인덱스를 생성해야 함. 

```
PUT /movie_term_completion
{
    "mappings" : {
        "_doc" : {
            "properties" : {
                "movieNmEnComple" : {
                    "type": "completion"
                }
            }
        }
    }
}

PUT /movie_term_completion/_doc/1
{
    "movieNmEnComple" : "After love"
}

PUT /movie_term_completion/_search
{
    "suggest" : {
    	"movie_completion": {
    		"prefix": "lo",
    		"completion" : {
    			"field": "movieNmEnComple",
    			"size" : 5
    		}
    	}
    }
}
```

## 맞춤법 검사기

### Term suggester API 를 이용한 맞춤법    

1. 자바카페 플러그인 설치  
    > ./elasticsearch-plugin install https://github.com/javacafe-project/elastic-book-etc/raw/master/plugin/javacafe-analyzer-6.4.3.zip  

2. 인덱스 생성
    ```
    PUT /company_spellchecker
    {
        "settings": {
            "index": {
                "analysis": {
                    "analyzer": {
                        "korean_spell_analyzer": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": [
                            "trim",
                            "uppercase",
                            "javacafe_spell"
                            ]
                        }
                    }
                }
            }
        }
    }
    ```

3. 매핑 설정  
    ```
    PUT /company_spellchecker/_doc/_mappings
    {
        "properties": {
        "name": {
            "type": "keyword",
            "copy_to":["suggest"]
        },
        "suggest": {
            "type": "completion",
            "analyzer": "korean_spell_analyzer"
        }
        }
    }
    ```
4. 데이터 추가
    ```
    PUT /company_spellchecker/_doc/1
    {
        "name" : "테슬라"
    }
    ```
5. 오타 교정 API
    ```
    POST /company_spellchecker/_doc/_search
    {
        "suggest": {
            "my-suggestion" : {
                "text": "톄슬라",
                "term": {
                    "field" : "suggest"
                }
            }
        }
    }
    ```

## 한영/영한 오타 교정
