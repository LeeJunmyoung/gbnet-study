# 데이터 집계
> SQL 에서 제공하는 Group By 연산과 비슷한 기능을 낼 수 있다.  


## Aggregation API
> 기존 검색 쿼리에 집계구문을 추가하는 방식으로 수행가능하다.

* 버킷 집계 : 쿼리 결과로 도출된 문서 집합에 대해 특정 기준으로 나눈 다음 나눠지 문서들에 대한 산술 연산을 수행. 이때 나눠진 문서들의 모음들이 버킷에 해당.  
* 메트릭 집계 : 쿼리 결과로 도출된 문서집합에서 필드의 값을 더하거나 평균을 내는등의 산술 연산  
* 파이프라인 집계 : 다른 집계또는 관련 메트릭 연산의 결과를 집계  
* 행렬 집계 : 버킷대상이 되는 도큐먼트의 여러 필드에서 추출한 값으로 행렬 연산을 수행. 아직은 공식 지원 X


```
{
    "query" : {....},
    "aggs" : {....}
}
```

### 집계구문 구조
```
"aggregations":{                        -- 데이터 집계하기 우해 맨 처음 명시해야함. aggs로 줄여서 쓰는것도 가능
    "<aggregation_name>":{              -- 하위 집계의 이름을 기입. 이름은 집계 결과 출력에 사용 따라서 사용자가 직접 임의의 이름 정의 
        "<aggregation_type>":{          -- 집계의 유형을 적음. 어떤 집계를 수행할 것인가를 나타냄. terms, data_histogram,sum 과 같은 다양한 집계유형 지정가능
            "<aggregation_body>"        -- aggregation_type에 맞춰 내용을 작성
        }
        [,"meta":{[<meta_data_body>]}]?
        [,"aggregations":{[<sub_aggregation>]+}]?
    }
    ,[,"<aggregation_name_2>:{...}"]*
}
```

### 집계 영역
```
{
    "query" : {         -- 쿼리의 질의를 수행 , 필드와 값이 일치하는 문서만 반환
        "constant_score" : {
            "filter" : {
                "match" : <필드조건>
            }
        }  
    },
    "aggs" : {          -- 질의를 통해 반환 받은 문서들의 집합 내에서 집계를 수행
        "<집계이름>" : {
            "<집계타입>" : {
                "field" : "<필드명>"
            }
        }
    }
}
```

## 합산 집계 (sum)
> Sum 기능.

```
# 얼마만큼에 BYTE 가 들어왔는지 확인 가능
# 질의가 명시 되지 않아 전체 검색 size=0 이기 때문에 결과 집합에 문서들 또한 존재 X
# 결과 문서가 출력되지 않더라도 실제 검색된 문서의 대상범위가 전체 문서이기 때문에 집계는 전체문서에 대해 수행.  
GET /apache-web-log/_search?size=0

{
    "aggs" : {
        "total_bytes" : {
            "sum" : {
                "field" : "bytes"
            }
        }
    }
}

GET /apache-web-log/_search?size=0
{
    "query" : {                    
        "constant_score" : {        -- 필터에 해당하는 문서들에 대해 동일한 스코어 부여
            "filter" : {
                "match" : {"geoip.city_name" : "paris"}
            }
        }
    },
    "aggs" : {
        "total_bytes" : {
            "sum" : {
                "field" : "bytes"
            }
        }
    }
}
```

## 최솟값 집계 (min)
> Min 기능.  

```
{
    "aggs" : {
        "min_bytes" : {
            "min" : {
                "field" : "bytes"
            }
        }
    }
}

GET /apache-web-log/_search?size=0
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "match" : { "geoip.city_name" : "paris" }
            }
        }
    },
    "aggs" : {
        "min_bytes" : {
            "min" : {
                "field" : "bytes"
            }
        }
    }
}
```

## 최댓값 집계 (max)
> Max 기능  

```
{
    "aggs" : {
        "max_bytes" : {
            "max" : {
                "field" : "bytes"
            }
        }
    }
}

GET /apache-web-log/_search?size=0
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "match" : { "geoip.city_name" : "paris" }
            }
        }
    },
    "aggs" : {
        max_bytes" : {
            "max" : {
                "field" : "bytes"
            }
        }
    }
}
```

## count 집계 (value_count)
> Count 기능

```
{
    "aggs" : {
        "bytes_count" : {
            "value_count" : {
                "field" : "bytes"
            }
        }
    }
}

GET /apache-web-log/_search?size=0
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "match" : { "geoip.city_name" : "paris" }
            }
        }
    },
    "aggs" : {
        bytes_count" : {
            "value_count" : {
                "field" : "bytes"
            }
        }
    }
}
```