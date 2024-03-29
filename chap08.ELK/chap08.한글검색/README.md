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

1. 인덱스 생성
    ```
    # 원본데이터 인덱스
    PUT /company
    {
        "settings": {
            "index": {
                "analysis": {
                    "analyzer": {
                        "my_analyzer": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": [
                                "trim",
                                "lowercase"
                            ]
                        }
                    }
                }
            }
        }
    }

    # 한영오차교정과 영한 오차교정을 위한 인덱스
    PUT /search_keyword
    {
        "settings": {
            "index": {
                "analysis": {
                    "analyzer": {
                        "kor2eng_analyzer": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": [
                                "trim",
                                "lowercase",
                                "javacafe_kor2eng"
                            ]
                        },
                        "eng2kor_analyzer": {
                            "type": "custom",
                            "tokenizer": "standard",
                            "filter": [
                                "trim",
                                "lowercase",
                                "javacafe_eng2kor"
                            ]
                        }
                    }
                }
            }
        }
    }
    ```

2. 매핑설정
    ```
    # 원본데이터 매핑설정
    PUT /company/_doc/_mappings
    {
        "properties": {
            "name": {
                "type" : "keyword"
            }
        }
    }

    # 한영 오타교정을 위한 매핑 설정
    PUT /search_keyword/_doc/_mapping
    {
        "properties": {
            "name": {
                "type": "keyword",
                "copy_to": ["kor2eng_suggest", "eng2kor_suggest"]
            },
            "kor2eng_suggest": {
                "type": "text",
                "analyzer": "standard",
                "search_analyzer": "kor2eng_analyzer"
            },
            "eng2kor_suggest": {
                "type": "text",
                "analyzer": "standard",
                "search_analyzer": "eng2kor_analyzer"
            }
        }
    }
    ```
3. 데이터 입력
    ```
    PUT /company/_doc/1
    {
        "name": "삼성전자"
    }
    PUT /company/_doc/2
    {
        "name": "apple"
    }
    
    PUT /search_keyword/_doc/1
    {
        "name": "삼성전자"
    }
    PUT /search_keyword/_doc/2
    {
        "name": "apple"
    }
    ```
4. 검색
    > 1. search_keyword 인덱스에 사용자 검색어를 가지고 검색질의  
    > 2. 검색결과가 없다면 검색어 그대로 company 인덱스에 검색 질의
    > 3. 검색 결과가 있다면 변경된 검색어로 company인덱스에 검색질의

    ```
    POST /search_keyword/_search
    {
        "query": {
            "match": {
                "eng2kor_suggest": {
                    "query": "tkatjdwjswk"
                }
            }
        }
    }

    POST /search_keyword/_search
    {
        "query": {
            "match": {
                "kor2eng_suggest": {
                    "query": "메ㅔㅣㄷ"
                }
            }
        }
    }
    ```

## 한글 키워드 자동완성    
     
### Completion Suggest API 를 이용한 한글 자동완성

1. 인덱스 생성
    ```
    PUT /ac_test
    {
        "settings": {
            "index": {
                "number_of_shards": 5,
                "number_of_replicas": 1
            },
            "analysis": {
                "analyzer": {
                    "ngram_analyzer": {
                        "type": "custom",
                        "tokenizer": "ngram_tokenizer",
                        "filter": [
                            "lowercase",
                            "trim"
                        ]
                    },
                    "edge_ngram_analyzer": {
                        "type": "custom",
                        "tokenizer": "edge_ngram_tokenizer",
                        "filter": [
                            "lowercase",
                            "trim",
                            "edge_ngram_filter_front"
                        ]
                    },
                    "edge_ngram_analyzer_back": {
                        "type": "custom",
                        "tokenizer": "edge_ngram_tokenizer",
                        "filter": [
                            "lowercase",
                            "trim",
                            "edge_ngram_filter_back"
                        ]
                    }
                },
                "tokenizer": {
                    "ngram_tokenizer": {
                        "type": "nGram",
                        "min_gram": "1",
                        "max_gram": "50",
                        "token_chars": [
                            "letter",
                            "digit",
                            "punctuation",
                            "symbol"
                        ]
                    },
                    "edge_ngram_tokenizer": {
                        "type": "edgeNGram",
                        "min_gram": "1",
                        "max_gram": "50",
                        "token_chars": [
                            "letter",
                            "digit",
                            "punctuation",
                            "symbol"
                        ]
                    }
                },
                "filter": {
                    "edge_ngram_filter_front": {
                        "type": "edgeNGram",
                        "min_gram": "1",
                        "max_gram": "50",
                        "side": "front"
                    },
                    "edge_ngram_filter_back": {
                        "type": "edgeNGram",
                        "min_gram": "1",
                        "max_gram": "50",
                        "side": "back"
                    }
                }
            }
        }
    }
    ```

2. 매핑 설정
    ```
    PUT /ac_test/_mapping/ac_test
    {
        "properties": {
            "item": {
                "type": "keyword",
                "boost": 30
            },
            "itemNgram": {
                "type": "text",
                "analyzer": "ngram_analyzer",
                "search_analyzer": "ngram_analyzer",
                "boost": 3
            },
            "itemNgramEdge": {
                "type": "text",
                "analyzer": "edge_ngram_analyzer",
                "search_analyzer": "ngram_analyzer",
                "boost": 2
            },
            "itemNgramEdgeBack": {
                "type": "text",
                "analyzer": "edge_ngram_analyzer_back",
                "search_analyzer": "ngram_analyzer",
                "boost": 1
            }
        }
    }
    ```
    - ## 원문  : 나보기가 엮겨워 가실 때에는

    1. Ngram 분석기    
        * 1단계 분석 : [나보기가, 엮겨워, 가실, 때에는]
        * 2단계 분석  
            [나, 나보, 나보기, 나보기가, 보, 보기, 보기가, 기, 기가, 가]  
            [엮, 엮겨, 엮겨워, 겨, 겨워, 워]  
            [가, 가실, 실]  
            [때, 때에, 때에는, 에, 에는, 는]  
    
    2. Edge Ngram 분석기
        * 1단계 : [나보기가, 엮겨워, 가실, 때에는]
        * 2단계  
            [나, 나보, 나보기, 나보기가]  
            [엮, 엮겨, 엮겨워]  
            [가, 가실]  
            [때, 때에, 때에는]

    3. Edge Ngram Back 분석기
        * 1단계 : [나보기가, 엮겨워, 가실, 때에는]  
        * 2단계   
            [나, 보, 나보, 기, 보기, 나보기, 가, 기가, 보기가, 나보기가]  
            [엮, 겨, 엮겨, 워, 겨워, 엮겨워]  
            [가, 실, 가실]
            [때, 에, 때에, 는, 에는, 때에는]


3. 데이터 입력
    ```
    POST /ac_test/_bulk
    { "index" : { "_index" : "ac_test", "_type" : "ac_test", "_id" : "1" } }
    { "item"  : "신혼", "itemNgram" : "신혼", "itemNgramEdge" : "신혼", "itemNgramEdgeBack" : "신혼" }

    { "index" : { "_index" : "ac_test", "_type" : "ac_test", "_id" : "2" } }
    { "item"  : "신혼가전", "itemNgram" : "신혼가전", "itemNgramEdge" : "신혼가전", "itemNgramEdgeBack" : "신혼가전" }

    { "index" : { "_index" : "ac_test", "_type" : "ac_test", "_id" : "3" } }
    { "item"  : "신혼가전특별전", "itemNgram" : "신혼가전특별전", "itemNgramEdge" : "신혼가전특별전", "itemNgramEdgeBack" : "신혼가전특별전" }


    ```

4. 검색  
    ```
    {
        "explain":false,
        "query": {
            "bool": {
            "should": [
                {
                "prefix": {
                    "item": "신혼가"
                }
                },
                {
                "term": {
                    "itemNgram": "신혼가"
                }
                },
                {
                "term": {
                    "itemNgramEdge": "신혼가"
                }
                },
                {
                "term": {
                    "itemNgramEdgeBack": "신혼가"
                }
                }
            ],
            "minimum_should_match": 1
            }
        }
    }
    ```
  
* 문제점.
    > 한글 자동 키워드 완성인데 '신혼' 에서 '싢'까지만 입력할 경우는???  

### 한글자모분석을 통한 자동완성
  
1.  인덱스생성  
    ```
    PUT /ac_test4
    {
        "settings" : {
            "index" : {
                "number_of_shards" : 5, 
                "number_of_replicas" : 1 
            },
            "analysis": {
                "analyzer": {
                    "jamo_index_analyzer": {
                        "type": "custom",
                        "tokenizer": "keyword",
                        "filter": [
                            "javacafe_jamo_filter",
                            "lowercase",
                            "trim",
                            "edge_ngram_filter_front"
                        ]
                    },
                    "jamo_search_analyzer": {
                        "type": "custom",
                        "tokenizer": "keyword",
                        "filter": [
                            "javacafe_jamo_filter",
                            "lowercase",
                            "trim"
                        ]
                    }			
                },
                "tokenizer" : {
                    "edge_ngram_tokenizer" : {
                        "type" : "edgeNGram",
                        "min_gram" : "1",
                        "max_gram" : "50",
                        "token_chars" : [
                            "letter",
                            "digit",
                            "punctuation",
                            "symbol"
                        ]
                    }
                },			
                "filter": {
                    "edge_ngram_filter_front" : {
                        "type" : "edgeNGram",
                        "min_gram" : "1",
                        "max_gram" : "50",
                        "side" : "front"
                    },
                    "javacafe_jamo_filter": {
                        "type": "javacafe_jamo"
                    }			
                }			
            }		
        }
    }
    ```
2. 매핑설정
    ```
    PUT /ac_test4/_mapping/ac_test4
    {
        "properties": {
            "item": {
                "type": "keyword",
                "boost": 30
            },
            "itemJamo": {
                "type": "text",
                "analyzer": "jamo_index_analyzer",
                "search_analyzer": "jamo_search_analyzer",
                "boost": 10
            }
        }
    }
    ```

3. 데이터 입력
    ```
    POST /ac_test4/_bulk

    {"index" : { "_index" : "ac_test4", "_type" : "ac_test4", "_id" : "1" }}
    {"item" : "신혼", "itemJamo" : "신혼"}

    {"index" : { "_index" : "ac_test4", "_type" : "ac_test4", "_id" : "2" }}
    {"item" : "신혼가전", "itemJamo" : "신혼가전"}

    {"index" : { "_index" : "ac_test4", "_type" : "ac_test4", "_id" : "3" }}
    {"item" : "신혼가전특별전", "itemJamo" : "신혼가전특별전"}
    ```

4. 검색
    ```
    POST /ac_test4/_search
    {
        "query" : {
            "bool" : {
                "should" : [{
                    "term" : {
                        "itemJamo" : "ㅅㅣㄴㅎ"
                    }
                }],
                "minimum_should_match" : 1
            }
        }
    }
    ```
