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



