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
```