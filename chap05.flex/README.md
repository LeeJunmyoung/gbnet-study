# Display (flex)
> 레이아웃을 구성하는 방법은 매우매우 다양하다.  
> 아주 먼 과거에 html에 table tag를 이용하여 구성해본 바 있다. 하지만 table tag 에 용도는 데이터들의 집합을 구성하기 위한 용도이며, 레이아웃을 구성하는데 적절치 못하다.  
> 그래서 이후에 css에 position을 이용한 레이아웃을 잡는 방식이 적용 되기 시작했다. 많이 선호되는 방식이며, 많이 활용되고 있다.  
> css3에 들어서며 더욱 세련되게 레이아웃을 구성할수 있는 flex가 등장함에 따라 많은 웹 페이지들이 flex로 레이아웃을 구성하고 있다.  
> 일명 flexbox라 불리는 Flexible Box module은 flexbox 인터페이스 내의 아이템 간 공간 배분과 강력한 정렬 기능을 제공하기 위한 1차원 레이아웃 모델 로 설계되었습니다.  
> grid도 있는데 그건 추후에 기회가 된다면 ....  
> flex 를 하는 이유는 react-native에 경우 레이아웃을 flex 로 한당.

## flex (부모 속성 - container)
1. flex-direction : item의 진행 방향을 지정한다.  
2. flex-wrap : item을 wrap 할것인지 아닌지를 지정.  
3. justify-content : flex-direction 기준(중심축)으로 정렬.  
4. align-items : flex-direction 교차 기준(교차축)으로 정렬.  
5. align-content : align-content 속성은 flex-wrap 속성의 동작을 변경할 수 있음.  


## flex (자식 속성 - item)
1. flex : flex 속성은 flex-grow, flex-shrink, flex-basis의 축약속성.   
2. ~~flex-grow : flexible item들이 차지할 너비들에 대한 증가형 숫자를 지정한다.~~  
3. ~~flex-shrink : flexible item들이 차지할 너비들에 대한 감소형 숫자를 지정한다.~~  
4. flex-basis : item의 길이를 지정한다.    
5. ~~order : flex들간의 순서를 변경할때 사용~~  