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

## 문서 생성
```
> POST localhost:9200/movie/_doc/1
# Json Data
{
	"movieCd"       : "1",
	"movieNm"       : "살아남은아이",
	"movieNeEn"     : "Last Child",
	"prdtYear"      : "2017",
	"openDt"        : "2017-10-24",
	"typeNm"        : "장편",
	"prdtStatNm"    : "기타",
	"nationAlt"     : "한국",
	"genreAlt"      : "드라마,가족",
	"repNationNm"   : "한국",
	"repGenreNm"    : "드라마"
}

# blocked by: [FORBIDDEN/12/index read-only / allow delete (api)] 에러시.
# 엘라스틱 서치는 용량 부족시 해당에러는 나타내게되고 아래와 같이 입력하면 된다.

> curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_cluster/settings -d 
'{ "transient": { "cluster.routing.allocation.disk.threshold_enabled": false } }'

> curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_all/_settings -d 
'{"index.blocks.read_only_allow_delete": null}'

해당 문서 생성시 _id가 생성되며 이 _id를 통해 조회가능하다.
```

## 문서 조회
```
# 단건
> GET localhost:9200/movie/_docs/{_id}

# 다건
> GET localhost:9200/movie/_docs/_search

# Request body 형식의 검색 질의
> POST /{index}/_search
{
    Json 쿼리
}

> POST /movie/_search
{
    "query" : {
        "term" : { "typeNm" : "장편" }
    }
}

# body(Json)

```

