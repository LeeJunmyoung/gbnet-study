# 분석기(고급검색)

## 한글 형태소 분석기
> 엘라스틱 서치에서 한글 문서를 효율적으로 검색하게 하기 위해 한글 형태소 분석기를 활용해 직접 분석기를 구성해야 함.  

### 은전한닢
> 은전 한닢은 Mecab-ko-dic 기반으로 만들어진 한국어 형태소 분석기로서 시스템 사전에 등록돼 있는 사전에 기반으로 동작하며, 복합명사와 활용어의 원형 찾기가 가능하다.  
> 라이센스는 아파치 2.0

[은전한닢 bitbucket](https://bitbucket.org/eunjeon/seunjeon/src/master/elasticsearch/)

### Nori형태소 분석기
> 루씬 프로젝트에서 공식적으로 제공되는 한글 형태소 분석기인 Nori는 엘라스틱 서치 6.4버전에서 공식적으로 릴리즈 됨.  
> 기존 형태소 분석기에 비해 30%이상 빠르고 메모리 사용량도 현저하게 줄이고 최적화 됨.  
> 아파치 라이센스 2.0  

```
bin/elasticsearch-plugin install analysis-nori
```

#### 1. nori_tokenizer
> 토크 나이저는 형태소를 토큰 형태로 분리하는데 사용.  
1. decompound_mode  
    > 토크나이저가 복합명사를 처리하는 방식을 결정. 복합명사가 있을 경우 단어를 어떻게 쪼갤지 정의  

    |파라미터값|설명|예제|
    |---|---|---|  
    |none|복합명사로 분리하지 않음|월미도<br>영종도|
    |discard|복합명사로 분리하고 원본 데이터는 삭제함|잠실역 => [잠실,역]|
    |mixed|복합명사로 분리하고 원본 데이터는 유지함|잠실역 => [잠실,역,잠실역]|

2. user_dictionary  
    > nori 토크나이저는 내부적으로 세종 말뭉치와 mecab-ko-dic 사전을 사용.   
    > user_dictionary를 이용해 사용자가 정희한 명사를 사전에 추가 등록할 수 있음.  
    > config/userdic_ko.txt형태로 생성해서 사용할수 있으며. 인덱스 매핑시 분석기의 파라미터로 사전 경로를 등록하면 됨.  
    - ex) 삼성전자  
    삼성전자 삼성 전자  

    ```
    # 분석기 정의
    PUT /{index} 
    {
        "settings" : {
            "index" : {
                "analysis" : {
                    "tokenizer" : {
                        "nori_user_dict_tokenizer" : { -- 토크나이저 명칭 정의
                            "type" : "nori_tokenizer",
                            "decompound_mode" : "mixed",    -- 토크나이저 복합명사 처리방식 결정
                            "user_dictionary" : "userdict_ko.txt" -- 유저 사전 등록
                        }
                    }
                }
            },
            "analyzer" : {
                "nori_token_analyzer" : { -- 분석기
                    "type" : "custom",
                    "tokenizer" : "nori_user_dict_tokenizer"
                }
            }
        }
    }    
    ```
    ```
    # 분석기 테스트
    POST /{index}/_analyze
    {
        "analyzer" : "nori_token_analyzer",
        "text" : "잠실역"
    }
    # 결과
    - 잠실 , 역 , 잠실역
    ```

    #### 2. nori_part_of_speech
    > nori_part_of_speech 토큰 필터는 품사 태그 세트와 일치하는 토큰을 찾아 제거하는 토큰 필터.  
    > 역색인될 명사를 선택적으로 고를 수 있음. 이를 통해 미사용할 형태소를 제거.  
    > 해당 토큰 필터는 stoptags라는 파라미터를 제공하는데, 이 파라미터를 이용해 분리된 토큰에서 제거할 특정 형태소를 지정하는것이 가능.  

    ```
    # 토큰 필터를 추가하기 위해 이미 생성된 설정정보를 변경하려면 인덱스를 close 상태로 만들어야 함.
    POST /{index}/_close

    # stoptags에 명사를 제외한 모든 형태소를 제거하도록 설정.
    PUT /{index}/_settings
    {
        "index" : {
            "analysis" : {
                "analyzer" : {
                    "nori_token_analyzer" : {
                        "tokenizer" : "nori_user_dict_tokenizer",
                        "filter" : [
                            "nori_posfilter"
                        ]
                    }
                },
                "filter" : {
                    "nori_posfilter" : {
                        "type" : "nori_part_of_speech",
                        "stoptags" : [
                            "E",
                            "IC",
                            "J",
                            ...
                        ]
                    }
                }
            }
        }
    }

    # default filter
    "stoptags": [
        "E",
        "IC",
        "J",
        "MAG", "MAJ", "MM",
        "SP", "SSC", "SSO", "SC", "SE",
        "XPN", "XSA", "XSN", "XSV",
        "UNA", "NA", "VSV"
    ]

    # 설정 정보가 업데이트 되면 인덱스를 다시 Open
    POST /{index}/_open
    ```

    [필터 파라미터](https://lucene.apache.org/core/8_4_0/analyzers-nori/org/apache/lucene/analysis/ko/POS.Tag.html)


    #### 3. nori_readingform 토큰 필터
    > nori_readingform 토큰 필터는 문서에 존재하는 한자를 한글로 변경하는 역할을 하는 필터.    


### 트위터 형태소 분석기
> 트위터 형태소 분석기는 트위터에서 한글을 처리하기 위해 개발한 형태소 분석기.  
> 초기에는 트위터에서 직접 개발했으나 2017년 4.4버전 이후로는 openkoreatext.org로 이관되어 오픈소스로 개발되고 있음.  
> 한글 형태소 분석 및 스테밍이 가능 (reduce , reducing, reduced)  
> 장점은 정규화, 토큰화, 스테밍, 어구 추출이 가능.  

##  검색 결과 하이라이트  
> 데이터를 검색할 때 highlight 옵션을 이용해 하이라이트를 수행할 필드를 지정.  

```
POST /movie/_search
{
    "query" : {
        "match" : {
            "title" : {
                "query" : "query"
            }
        },
        "hightlight" : {
            "field" : {
                "title" : {}
            },
            "pre_tags" : [
                "<strong>"
            ],
            "post_tags" : [
                "</strong>"
            ]
        }
    }
}
```