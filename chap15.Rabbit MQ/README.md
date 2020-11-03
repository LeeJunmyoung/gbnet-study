# Rabbit MQ

## MQ 란?
> MQ(Message Queue)란 메시지를 이용하여 여러 서비스들을 연결해주는 미들웨어 솔루션.  

### 장점
- 비동기 : Queue에 넣기 때문에 나중에 처리할 수 있습니다.
- 비동조 : Application과 분리할 수 있습니다.
- 탄력성 : 일부가 실패 시 전체는 영향을 받지 않습니다
- 과잉 : 실패할 경우 재실행이 가능합니다
- 확장성 : 다수의 프로세스들이 큐에 메시지를 보낼 수 있습니다.

<br>

![mq](./image/mq.png)

### 출처 
- https://steady-snail.tistory.com/165