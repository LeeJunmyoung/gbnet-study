# 한글 형태소 분석기를 이용한 검색예제

## 엘라스틱 서치 설치
```
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.6.1-linux-x86_64.tar.gz
tar -xvzf elasticsearch-7.6.1-linux-x86_64.tar.gz
```

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
동물원축제 동물원 축제
현대그린푸드 현대 그린 푸드

한줄에 해당하는 형태로만 넣어주어야함!!
```
#### 샤드와 레플리카  
[참고 사이트][https://www.kdata.or.kr/info/info_04_view.html?field=&keyword=&type=techreport&page=29&dbnum=180207&mode=detail&type=techreport]
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


## 실제 데이터를 형태소로 검색해보자.

### 인덱스 생성
```
# PUT http://localhost:9200/meatbox

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
```

### 매핑 설정
```
# PUT http://localhost:9200/meatbox/_mapping/data

{
	"properties" : {
        "category_product_seq" : {
            "type" : "keyword"
        },
        "category_code" : {
            "type" : "keyword"
        },
         "category_name" : {
            "type" : "keyword"
        },
         "main_category_yn" : {
            "type" : "keyword"
        },
         "product_seq" : {
            "type" : "keyword"
        },
         "product_name" : {
            "type" : "text"
            , "analyzer" : "nori_analyzer"
        },
         "search_text" : {
            "type" : "keyword"
        },
         "status" : {
            "type" : "keyword"
        },
        "sell_unit_type" : {
            "type" : "keyword"
        },
        "keeping" : {
            "type" : "keyword"
        },
        "origin" : {
            "type" : "keyword"
        },
        "brand" : {
            "type" : "keyword"
        },
        "grade" : {
            "type" : "keyword"
        },
        "purpose" : {
            "type" : "keyword"
        },
        "sort_ranking" : {
            "type" : "keyword"
        },
        "price" : {
            "type" : "long"
        },
        "unit_type" : {
            "type" : "keyword"
        },
        "review_cnt" : {
            "type" : "long"
        },
        "review_star_point" : {
            "type" : "keyword"
        },
        "last_incomming" : {
            "type" : "keyword"
        },
        "kind" : {
            "type" : "keyword"
        },
        "proc" : {
            "type" : "keyword"
        },
        "item_cat" : {
            "type" : "keyword"
        },
        "item_cat_code" : {
            "type" : "keyword"
        },
        "thumbnail_url" : {
            "type" : "keyword"
        },
        "product_url" : {
            "type" : "keyword"
        },
        "delivery_price" : {
            "type" : "keyword"
        },
        "mark_event_url" : {
            "type" : "keyword"
        },
        "mark_cert_1_url" : {
            "type" : "keyword"
        },
        "mark_cert_2_url" : {
            "type" : "keyword"
        },
        "mark_cert_3_url" : {
            "type" : "keyword"
        },
        "mark_cert_4_url" : {
            "type" : "keyword"
        },
        "special" : {
            "type" : "keyword"
        },
        "etc_display_text" : {
            "type" : "keyword"
        },
        "etc_display_strike_gbn" : {
            "type" : "keyword"
        },
        "card_yn" : {
            "type" : "keyword"
        },
        "item_desc" : {
            "type" : "keyword"
        },
        "search_all" : {
            "type" : "text"
            , "analyzer" : "nori_analyzer"
        },
        "stock_amount" : {
            "type" : "long"
        },
        "incoming_due_date" : {
            "type" : "keyword"
        },
        "item_seqs" : {
            "type" : "keyword"
        },
        "event_msg" : {
            "type" : "keyword"
        },
        "base_box_cnt" : {
            "type" : "keyword"
        },
        "large_purchase_gbn" : {
            "type" : "keyword"
        },
        "moddate":{
        	"type" : "keyword"
        },
        "del_yn":{
        	"type" : "keyword"
        },
        "top_category_seq":{
        	"type" : "keyword"
        },
        "base_sell_yn":{
        	"type" : "keyword"
        },
        "sell_member_seq":{
        	"type" : "keyword"
        },
        "meat_loan_yn": {
        	"type" : "keyword"
        },
        "regdate": {
        	"type" : "keyword"
        }
	}
}
```

### 파일 import
```
curl -H "Content-Type: application/x-ndjson" -XPOST "localhost:9200/meatbox/data/_bulk?pretty&refresh" --data-binary "@file.json"

file.json
{"index":{}} \n
{"category_product_seq":179434,"category_code":10001,"category_name":"소고기 > 한우거세","main_category_yn":"Y","product_seq":156454,"product_name":"등심 - 국산 | 꿈에그린한우 - 1+등급","search_text":"","status":"판매종료","sell_unit_type":"B","keeping":"냉장","origin":"국산","brand":"꿈에그린한우","grade":"1+등급","purpose":"꽃등심|등심|스테이크","sort_ranking":100,"price":62800,"unit_type":"KG","review_cnt":0,"review_star_point":"","last_incomming":"2019-08-08 19:01:06","kind":"한우거세","proc":"원물","item_cat":"등심","item_cat_code":"149","thumbnail_url":"/m/images/itemImage/2019/08/08/19/01/thumb_productThumbnail_71617_20190808190105.jpg","product_url":"-","delivery_price":"무료배송","mark_event_url":"KP01","mark_cert_1_url":"Y","mark_cert_2_url":"N","mark_cert_3_url":"N","mark_cert_4_url":"N","special":"","etc_display_text":"","etc_display_strike_gbn":"N","card_yn":"Y","item_desc":"","search_all":"등심 - 국산 | 꿈에그린한우 - 1+등급 한우거세 원물 등심 냉장 국산 꿈에그린한우 1+등급 꽃등심|등심|스테이크 ","stock_amount":0,"incoming_due_date":"","item_seqs":"","event_msg":"","base_box_cnt":1,"large_purchase_gbn":"N","moddate":"2019-08-10 00:23:00","del_yn":"Y","top_category_seq":1000,"base_sell_yn":"N","sell_member_seq":71617,"meat_loan_yn":"N","regdate":"2019-08-08 19:01:06"} \n
{"index":{}} \n
{"category_product_seq":179433,"category_code":10001,"category_name":"소고기 > 한우거세","main_category_yn":"Y","product_seq":156453,"product_name":"등심 - 국산 | 꿈에그린한우 - 1++등급","search_text":"","status":"판매종료","sell_unit_type":"B","keeping":"냉장","origin":"국산","brand":"꿈에그린한우","grade":"1++등급","purpose":"꽃등심|등심|스테이크","sort_ranking":100,"price":72500,"unit_type":"KG","review_cnt":0,"review_star_point":"","last_incomming":"2019-08-08 18:59:35","kind":"한우거세","proc":"원물","item_cat":"등심","item_cat_code":"149","thumbnail_url":"/m/images/itemImage/2019/08/08/18/59/thumb_productThumbnail_71617_20190808185934.jpg","product_url":"-","delivery_price":"무료배송","mark_event_url":"KP01","mark_cert_1_url":"Y","mark_cert_2_url":"N","mark_cert_3_url":"N","mark_cert_4_url":"N","special":"","etc_display_text":"","etc_display_strike_gbn":"N","card_yn":"Y","item_desc":"","search_all":"등심 - 국산 | 꿈에그린한우 - 1++등급 한우거세 원물 등심 냉장 국산 꿈에그린한우 1++등급 꽃등심|등심|스테이크 ","stock_amount":0,"incoming_due_date":"","item_seqs":"","event_msg":"","base_box_cnt":1,"large_purchase_gbn":"N","moddate":"2019-08-17 20:50:04","del_yn":"Y","top_category_seq":1000,"base_sell_yn":"N","sell_member_seq":71617,"meat_loan_yn":"N","regdate":"2019-08-08 18:59:35"} \n

주의 !!! \n 단위로 데이터 삽입됨.
```


### 검색
```
# POST http://localhost:9200/meatbox/data/_search

{ 
    "query":{ 
        "bool":{ 
            "must":{ 
                "multi_match":{ 
                    "query":"BBQ",
                    "fields":[ 
                        "category_name",
                        "search_all"
                    ]
                }
            },
            "filter":[ 
                { 
                    "term":{ 
                        "status":"판매중"
                    }
                },
                { 
                    "range":{ 
                        "price":{ 
                            "gte":500
                        }
                    }
                }
            ]
        }
    }
}
```

