# Rabbit MQ

## 1 MQ 란?
> MQ(Message Queue)란 메시지를 이용하여 여러 서비스들을 연결해주는 미들웨어 솔루션.  

### 1.1 장점
- 비동기 : Queue에 넣기 때문에 나중에 처리할 수 있습니다.
- 비동조 : Application과 분리할 수 있습니다.
- 탄력성 : 일부가 실패 시 전체는 영향을 받지 않습니다
- 과잉 : 실패할 경우 재실행이 가능합니다
- 확장성 : 다수의 프로세스들이 큐에 메시지를 보낼 수 있습니다.

<br>

![mq](./image/mq.png)

## 2 Rabbit MQ
> AMQP 를 구현한 오픈 소스 메시지 브로커 소프트웨어이며, STOMP, MQTT 및 기타 프로토콜을 지원한다.
> AMQP(Advanced Message Queuing Protocol)의 기능들은 메시지 지향, 큐잉, 라우팅(P2P 및 발행-구독), 신뢰성, 보안이다.

![mq](./image/amqp_exam.png)

### 2.1 용어 정리
명칭 | 설명
---|---
Publisher(Producer) | 메시지를 생성하고 발송하는 주체.
Publish(Producing)  | 메시지를 전송하는 행위
Exchange            | Publisher(Producer)로 부터 수신한 메시지를 큐에 분배하는 라우터 역할
Routes              | 들어온 메시지를 Queue의 분배하는 행위
Queue               | 메시지를 메모리나 디스크에 저장했다가 Consumer에게 메시지를 전달 하는 역할
Binding             | Exchange와 Queue의 관계를 정의
Consumes(Subscribe) | Consumer가 Queue에 접근하여 메시지를 가져오는 행위.
Consumer            | 메시지를 가져오는 주체.

<br>
<br>

### 2.2 Exchanges and Exchange Types
Exchange type | Default pre-declared names | 설명
---|---|---
Direct exchange     | (Empty string) and amq.direct | 라우팅 키를 이용해 메시지를 라우팅 하고, 하나의 큐에 여러개의 라우팅 키를 지정할 수 있다.
Fanout exchange     | amq.fanout | exchange에 등록된 모든 큐에 메세지를 전송한다.
Topic exchange      | amq.topic | 라우팅 키 패턴을 이용하여 메세지를 라우팅한다.
Headers exchange    | amq.match (and amq.headers in RabbitMQ) | 라우팅 키 말고 헤더를 사용하여 라우팅한다.

## 3 MQ 맛보기

### 3.1 설치
```
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# management - guest / guest
```

### 3.2 큐 생성
![mq](./image/add_queue.png)

### 3.3 토픽 바인딩
![mq](./image/add_bind.png)

### 3.4 publish message
![mq](./image/publish_msg.png)

### 3.5 메시지 확인
![mq](./image/get_message.png)

### 3.6 구독
![mq](./image/subscribe_java.png)

### 출처 
- https://steady-snail.tistory.com/165
- https://www.rabbitmq.com/tutorials/amqp-concepts.html