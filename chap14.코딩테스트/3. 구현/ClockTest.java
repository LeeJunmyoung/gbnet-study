/**
 * 정수 N 이 입력되면 00시 00분 00초 부터 N시 59분 59초 까지의 모든 시각중에서 3이 하나라도 포함되는
 * 모든 경우의 수를 구하시오.
 * 
 * 첫째 줄에 정수 N이 입력된다(0 <= N <= 23)
 * 출력 조건 00시 00분 부터 N시 59분 59초 까지의 모든 시각중에서 3이 하나라도 포함 되는 경우의 수를 출력.
 * 
 * input 5
 * output 11475
 * 
 * - 하루는 60초 * 60분 * 24시간 이므로 86400가지의 경우의 수를 가지므로
 * 완전 탐색 알고리즘을 사용한다.
 * 
 */
public class ClockTest {

	public static void main(String[] args) {
		int N = 5;
		
		System.out.println(getClockCnt(N));
	}
	
	public static int getClockCnt(int N) {
		int cnt = 0;
		
		for(int hour = 0; hour <= N; hour++) {
			for(int min = 0; min < 60; min++) {
				for(int sec = 0; sec < 60; sec++) {
					if(String.valueOf(hour).contains("3") || String.valueOf(min).contains("3") || String.valueOf(sec).contains("3"))
					//if((hour+":"+min+":"+sec).contains("3"))
						cnt++;
				}
			}
		}
		return cnt;
	}

}
