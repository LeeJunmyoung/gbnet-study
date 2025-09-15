/**
 * 정수 X가 주어질 때 정수 X에 사용할 수 있는 연산은 다음과 같다.
 * 1. X가 5로 나누어떨어지면 5로 나눈다.
 * 2. X가 3으로 나누어 떨어지면 3으로 나눈다
 * 3. X가 2로 나누어 떨어지면 2로 나눈다.
 * 4. X에서 1을 뺀다.
 * 
 * 정수 X가 주어졌을 때, 연산 4개를 적절히 사용해서 1을 만드려고 한다. 연산을 사용하는
 * 횟수의 최솟값을 구하시오.
 * 
 * X = 26
 * 
 * 1. 26 - 1 = 25
 * 2. 25 / 5 = 5
 * 3. 5 / 5	 = 1
 * 
 * 출력
 * 3
 *
 */

public class CalcOneTest {

	public static void main(String[] args) {
		System.out.println(calc(26)); // 25(-1), 5(/5), 1(/5)
		System.out.println(calc(17)); // 16(-1), 15(-1), 3(/5), 1(/3)
	}
	
	public static int calc(int x) {
		int[] dpMemory = new int[x + 1];
		dpMemory[1] = 0; // 초기화
		for(int num = 2; num < x + 1; num++) {
			// 현재의 수에서 1을 빼는경우
			dpMemory[num] = dpMemory[num - 1] + 1;
			
			// 현재의 수가 5로 나누어 떨어질때
			if(num % 5 == 0)
				dpMemory[num] = Math.min(dpMemory[num] , dpMemory[num / 5] + 1);
			
			// 현재의 수가 3으로 나누어 떨어질때
			else if(num % 3 == 0)
				dpMemory[num] = Math.min(dpMemory[num] , dpMemory[num / 3] + 1);
			
			// 현재의 수가 2로 나누어 떨어질때
			else if(num % 2 == 0)
				dpMemory[num] = Math.min(dpMemory[num] , dpMemory[num / 2] + 1);
		}
		
		return dpMemory[x];
	}
}
