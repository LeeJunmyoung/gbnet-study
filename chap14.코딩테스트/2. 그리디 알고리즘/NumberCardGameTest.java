import java.util.Arrays;
import java.util.Collections;

/**
 * 숫자 카드 게임은 여러 개의 숫자 카드 중에서 가장 높은 숫자가 쓰인 카드 한장을 뽑는 게임이다.
 * 단, 아래와 같은 룰을 지켜야한다.
 * 
 * 1. 숫자가 쓰인 카드들이 N * M 형태로 놓여 있다. 이 때 N은 행의 개수를 의미하며, M은 열의 개수를 의미한다.
 * 2. 먼저 뽑고자 하는 카드가 포함 되어 있는 행을 선택한다.
 * 3. 그 다음 선택된 행에 포함된 카드들 중 가장 숫자가 낮은 카드를 뽑아야한다.
 * 4. 따라서 처음에 카드를 골라낼 행을 선택 할 때, 이후에 해당 행에서 가장 숫자가 낮은 카드를 뽑을 것을
 * 고려하여 최종적으로 가장 높은 숫자의 카드를 뽑을 수 있도록 전략을 세워야 한다.
 * 
 * 3 1 2
 * 4 1 4
 * 2 2 2
 * 의 경우 2
 *
 */
public class NumberCardGameTest {

	public static void main(String[] args) {
		int arr[][] = {
						{3, 1, 2}
						, {4, 1, 4}
						, {2, 2, 2}
					};
		
		System.out.println(solutionPrimitive(arr));
		System.out.println(solutionReferenceType(arr));
	}
	
	public static int solutionPrimitive(int arr[][]) {
		int answer = 0;
		
		int[] minArr = new int[arr.length]; // 각 행만큼의 배열을 만들어준다. 
		
		for(int num = 0; num < arr.length; num++) {
			minArr[num] = min(arr[num]);
			
		}
		
		answer = max(minArr);
		
		return answer;
	}
	
	public static int solutionReferenceType(int arr[][]) {
		int answer = 0;
		
		Integer[] minArr = new Integer[arr.length]; // 각 행만큼의 배열을 만들어준다. 
		
		for(int num = 0; num < arr.length; num++) {
			Integer[] arrInteger = Arrays.stream(arr[num]).boxed().toArray(Integer[]::new);
			minArr[num] = Collections.min(Arrays.asList(arrInteger));
		}
		
		answer = Collections.max(Arrays.asList(minArr));
		
		return answer;
	}
	
	public static int min(int arr[]) {
		int min = Integer.MAX_VALUE;
		
		for(int a : arr) {
			if(min > a)
				min = a;
		}
		
		return min;
	}
	
	public static int max(int arr[]) {
		int max = Integer.MIN_VALUE;
		
		for(int a : arr) {
			if(max < a)
				max = a;
		}
		
		return max;
	}

}
