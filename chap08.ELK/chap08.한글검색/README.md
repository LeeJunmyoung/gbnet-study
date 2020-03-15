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
```