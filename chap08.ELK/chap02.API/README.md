# API

## 인덱스 생성
```
> PUT localhost:9200/movie
# Json Data
{
    "setting": {
        "number_of_shards" : 3,
        "number_of_replicas" : 2
    },
    "mappings" : {
        "_doc" : {
            "properties" : {
                "movieCd" :         { "type" : "integer"    },
                "movieNm" :         { "type" : "text"       },
                "movieNeEn" :       { "type" : "text"       },
                "prdtYear" :        { "type" : "integer"    },
                "openDt" :          { "type" : "date"       },
                "typeNm" :          { "type" : "keyword"    },
                "prdtStatNm" :      { "type" : "keyword"    },
                "nationAlt" :       { "type" : "keyword"    },
                "genreAlt" :        { "type" : "keyword"    },
                "repNationNm" :     { "type" : "keyword"    },
                "repGenreNm" :      { "type" : "keyword"    }
            }
        }
    }
}
```

## 인덱스 삭제
```
> DELETE localhost:9200/movie
```

