# Secure Coding (Java)

## SQL Injection
> SQL Injection 은 사용자가 입력한 데이터를 검증하지 않은 채로 쿼리문의 일부로 활용할 때 발생 할수 있는 취약점이다.   
> 이러한 취약점으로 인하여 데이터가 무단으로 조회되거나 인증과정이 부적절하게 수행되는 등의 문제점이 발생할 수 있다.

### 조치.
1. Filter로 검증한다.
2. Interceptor로 검증한다.
3. 라이브러리나 Validator로 검증한다.
4. Mybatis 쿼리문에서 #{}을 사용할 경우 JDBC PreparedStatement를 사용하게 되므로 쿼리문의 구조가 변경되지 않는다.   
    -${}을 사용할 경우 매개변수 값을 문자열 연결 방식으로 취환하므로 쿼리문의 구조가 변경 될수 있음.  
    1. statement: Statement 인터페이스는 CREATE , ALTER , DROP 등과 같은 DDL 문에 사용됩니다 . 이 인터페이스를 사용하여 런타임에 매개 변수를 SQL 쿼리에 전달할 수 없습니다.  
    2. PreparedStatement: 동적 또는 매개 변수화 된 SQL 쿼리를 실행하는 데 사용됩니다. 이 인터페이스를 사용하여 런타임에 SQL 쿼리에 매개 변수를 전달할 수 있습니다. 특정 SQL 쿼리를 여러 번 실행하는 경우 PreparedStatement를 사용하는 것이 좋습니다.   
    PreparedStatement가 사전 컴파일되고 쿼리 계획을 생성하는 횟수와 관계없이 쿼리 계획이 한 번만 만들어지기 때문입니다. 이렇게하면 많은 시간을 절약 할 수 있습니다.
    3.CallableStatement: 저장 프로 시저를 실행하는 데 사용됩니다.
    [참고 사이트](https://javaconceptoftheday.com/statement-vs-preparedstatement-vs-callablestatement-in-java/)


