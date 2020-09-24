import java.util.Arrays;
import java.util.Collections;

/**
 * 다양한 수로 이루어진 배열이 있을때 주어진 수들을 M번 더하여 가장 큰 수를 만드는 법칙이다.
 * 단, N개의 숫자로 이루어진 배열이다.
 * 단, 배열의 특정한 인덱스에 해당하는 수가 연속해서 K번을 초과하여 더해질 수 없다.
 * 단, 서로 다른 인덱스에 해당하는 수가 같은 경우에도 서로 다른것으로 간주한다.
 * 
 * 예를 들어, 2,4,5,4,6으로 이루어진 배열이 있을때 M이 8이고 K가 3이라면
 * 6 + 6 + 6 + 5 + 6 + 6 + 6 + 5 = 46 이 된다
 * 
 * 예를 들어 3, 4, 1, 2, 4으로 이루어진 배열이 M이 7이고 K가 2라면
 * 4 + 4 + 4 + 4 + 4 + 4 + 4 = 28이 된다.
 *
 * 2 <= N <= 1000
 * 1 <= M <= 10000
 * K <= K <= 10000
 */

public class BigNumberRule {

	public static void main(String[] args) {
		int[] N = {2, 4, 5, 4, 6};
		
		System.out.println(solution(N, 8, 3));
	}
	
	public static int solution(int N[], int M, int K) {
		int answer = 0;
		
		Integer[] nArr = Arrays.stream(N).boxed().toArray(Integer[]::new); // Integer 배열 생성
		Arrays.sort(nArr, Collections.reverseOrder()); // 역순으로 정렬하기 위해 레퍼런스 타입으로 만들어준거임.
		// Arrays.sort(arr); //가능 근데 그냥 역순으로 정렬하고 싶음.
		
		int first	= nArr[0];
		int second	= nArr[1];
		
		int cnt = (M / (K + 1)) * K; // 가장 큰수의 횟수 
		
		answer += cnt * first;			// 가장큰수 먼저 더하기
		answer += (M - cnt) * second;	// 두번째로 큰수 더하기
		
		return answer;
	}

}
