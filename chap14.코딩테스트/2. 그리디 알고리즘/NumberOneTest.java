/**
 * 
 * 어떠한 수 N 이 1 이 될때까지 다음의 두과정 중 하나를 반복적으로 선택하여 수행하려한다.
 * 
 * 1. N 에서 1을 뺀다
 * 2. N을 K로 나눈다. 단, 두번째 연산은 N이 K로 나누어 떨어질때만 사용한다.
 *
 * N 이 1이 될때까지의 최소 횟수를 구한다.
 */

public class NumberOneTest {

	public static void main(String[] args) {
		System.out.println(solution(25, 3));
		System.out.println(solutionRecursive(25, 3));
	}
	
	public static int solution(int N, int K) {
		int cnt = 0;
		while(N > 1) {
			if(N % K == 0) {
				N = N / K;
			}
			else {
				N--;
			}
			cnt++;
		}
		
		return cnt;
	}
	
	public static int solutionRecursive(int N, int K) {
		if(N == 1) {
			return 0;
		}
		else if(N % K == 0) {
			return solutionRecursive(N / K, K) + 1;
		}
		else {
			return solutionRecursive(N - 1, K) + 1;
		}
	}

}
