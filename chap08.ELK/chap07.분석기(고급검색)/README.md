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
POST /{index}/_search
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

## 동적 필드 수정
```
# 필드 추가
POST /{index}/doc/{id}/_update
{
    "script" : "ctx._source.{field_name} = {value}"
}

# 필드 제거
POST /{index}/doc/{id}/_update
{
    "script" : "ctx._source.remove(\"{field_name}\")"
}
```

## 검색 템플릿
> 검색 템플릿의 필드명과 파라미터를 사용해 쿼리를 전송하고 템플릿에 제공한 파라미터로 실제 검색이 이뤄짐. 검색 템플릿은 mustache라는 템플릿을 사용
```
# 검색 템플릿 등록
POST _script/{template_name}
{
    "script" : {
        "lang" : "mustache", 
        "source" : {
            "query" : {
                "match" : {
                    "movieNm" : "{{ movie_name }}"
                }
            }
        }
    }
}

# 템플릿을 통한 검색
POST /{index}/_doc/_search/template
{
    "id" : "{template_name}",
    "params" : {
        "movie_name" : "harry"
    }
}
```

## 별칭을 이용해 최신 인덱스 유지.
> 엘라스틱서치를 운영중 인덱스 매핑 설정이 변경되거나 인덱스가 깨진다면 기존에 생성된 인덱스를 삭제하고 다시 생성해야 함.  
> 하지만 운영중인 서비스에 인덱스를 삭제할 순 없다.  
> 이러한 문제를 극복하기 위해 별칭(alias) 기능을 제공한다.  
> 별칭을 사용해 익덱스가 추가되거나 삭제될 경우 새로운 인덱스로 사용자 요청이 자연스럽게 이동하도록 유도.  
```
# 1. 인덱스 복사
POST /_reindex
{
    "source" : {
        "index" : "movie_20200308"
    },
    "dest" : {
        "index" : "{new_index}"
    }
}

# 별칭 변경
POST /_aliases
{
    "actions" : [
        {"delete" : {"index" : "movie_20200308", "alias" : "movie"}},
        {"add" : {"index" : "movie_20200309", "alias" : "movie"}},
    ]
}

```

## 스냅샷응 이용한 백업/복구
> 클러스터와 인덱스의 데이터가 커질수록 백업의 필요성도 커진다.  
> 이러한 문제를 해결하기 위해 _snapshot API를 제공한다.  
> 스냅샷 기능을 이용해 개별 인덱스를 백업 할 수도 있고, 클러스터 전체를 스냅샷으로 만드는 것도 가능.  
```
# backup폴더 생성.
# config 폴더에 elasticsearch.yml 에 스냅숏을 사용하도록 지정.
> path.repo: ["{dir_path}"]

# 백업 repository 생성
PUT /_snapshot/{repo}
{
    "type" : "fs",
    "settings" : {
        "location" : "{dir_path}",
        "compress" : true
    }
}

# movie_snap_20200309 스냅샷 생성 
PUT /_snapshot/{repo}/{snap_name}?wait_for_completion=true
{
    "indices" : "movie_20200309",
    "ignore_unavailable" : true,
    "include_global_state" : false
}

# 스냅샷 확인
GET /_snapshot/movie/_all

# 스냅샷 복구
- movie_20200309 인덱스가 복구된다.
- 하지만 인덱스가 있다면 복구는 실패한다.
POST /_snapshot/{repo}/{snap_name}/_restore

# 스냅샷 삭제
DELETE /_snapshot/{repo}/{snap_name}


```