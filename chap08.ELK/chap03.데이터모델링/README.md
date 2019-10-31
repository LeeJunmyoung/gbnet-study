# 데이터 모델링
> 엘라스틱 서치에서는 색인할 때 문서의 데이터 유형에 따라 필드에 적절한 데이터 타입을 지정  
> 매핑은 색인될 문서의 데이터 모델링이라고도 할수 있음.  
> 사전에 매핑을 설정하면 지정된 데이터 타입으로 색인되지만 매핑을 설정해두지 않으면 엘라스틱 서치가 자동으로 필드를 생성하고 플드 타입까지 결정  
> 매핑은 사람이 직접 !   
> 한번 생성된 매핑의 타입은 변경 불가. 타입을 변경하려면 인덱스를 삭제한 후 다시 생성하거나 매핑을 다시 정의해야 한다.  

## 매핑 인덱스 만들기

### 예시

#### 영화 정보 문서의 구조
매핑명|필드명|필드타입
-----|-----|-----
인덱스 키|movieCd|keyword
영화제목_국문|movieNm|text
영화제목_영문|movieNmEn|text
제작년도|prdtYear|integer
개봉년도|openDt|integer
영화유형|typeNm|keyword
제작상태|prdtStatNm|keyword
제작국가(전체)|nationAlt|keyword
장르(전체)|genreAlt|keyword
대표제작국가|repNationNm|keyword
대표장르|repGenreNm|keyword
영화감독명|directors.peopleNm|object->keyword
제작사코드|companies.companyCd|object->keyword
제작사명|companies.companyNm|object->keyword
