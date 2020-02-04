# 한글 형태소 분석기를 이용한 검색예제

## Nori 설치 
> ES6.4부터 한글 형태소 분석기 nori가 추가됨.
```
# 플러그인 설치
./bin/elasticsearch-plugin install analysis-nori
```

## 형태소 분석기 분석

### 형태소 분석기 사용을 위한 test 인덱스 생성
```
# PUT http://localhost:9200/test
 
{
    "settings":{
        "number_of_shards": 5,
        "number_of_replicas": 0,
        "analysis":{
            "tokenizer":{
                "nori_user_dictionary":{
                    "type":"nori_tokenizer",
                    "decompound_mode":"mixed",
                    "user_dictionary":"user_dictionary.txt"
                }
            },
            "analyzer":{
                "nori_analyzer":{
                    "type":"custom",
                    "tokenizer":"nori_user_dictionary"
                }
            }
        }
    }
}

# conf/user_dictionary.txt
동물원 축제
```
#### 샤드와 레플리카  
![참고 사이트](https://www.kdata.or.kr/info/info_04_view.html?field=&keyword=&type=techreport&page=29&dbnum=180207&mode=detail&type=techreport)
#### decompound_mode
1. none : 복합 명사로 분리하지 않음. ex 명동역 => 명동역
2. discard : 복합 명사로 분리하고 원본 데이터는 삭제. ex 명동역 => 명동, 역
3. mixed : 복합 명사로 분리하고 원본데이터도 남겨둠. ex 명동역 => 명동역, 명동, 역

### 형태소 검색기로 분석해보기
```
# POST http://localhost:9200/test/_analyze
# request
{
    "analyzer":"nori",
    "text":"동물원축제"
}

# response
{
    "tokens": [
        {
            "token": "동물",
            "start_offset": 0,
            "end_offset": 2,
            "type": "word",
            "position": 0
        },
        {
            "token": "원",
            "start_offset": 2,
            "end_offset": 3,
            "type": "word",
            "position": 1
        },
        {
            "token": "축제",
            "start_offset": 3,
            "end_offset": 5,
            "type": "word",
            "position": 2
        }
    ]
}

# request
{
    "analyzer":"nori_analyzer",
    "text":"동물원축제"
}

# response 
{
    "tokens": [
        {
            "token": "동물원",
            "start_offset": 0,
            "end_offset": 3,
            "type": "word",
            "position": 0,
            "positionLength": 2
        },
        {
            "token": "동물",
            "start_offset": 0,
            "end_offset": 2,
            "type": "word",
            "position": 0
        },
        {
            "token": "원",
            "start_offset": 2,
            "end_offset": 3,
            "type": "word",
            "position": 1
        },
        {
            "token": "축제",
            "start_offset": 3,
            "end_offset": 5,
            "type": "word",
            "position": 2
        }
    ]
}
```
