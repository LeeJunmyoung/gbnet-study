import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 
 * 하나의 수열에는 다양한 수가 존재한다. 이러한 수는 크기에 상관없이 나열되어 있다. 이 수를 큰 수 부터 작은 수의 순서로 정렬해야 한다.  
 * 
 * 조건 
 * - 첫째 줄에 수열에 속해 있는 수의 개수가 주어진다. 1 <= N <= 500
 * - 둘째 줄부터 N+1 까지 N개의 수가 입력된다. 수의 범위는 1이상 100,000이하의 자연수이다.
 * 
 * 출력
 * - 입력으로 주어진 수열이 내림차순으로 정렬된 결과를 공백으로 구분하여 출력한다.
 * 
 * ex)
 * - input
 * 3
 * 15
 * 27
 * 12
 * 
 * - output
 * 27 15 12
 * 
 */

public class UpDownTest {

	public static void main(String[] args) {
		int arr[] = input();
		
		String output = Arrays.stream(arr)
								.boxed()
								.sorted(Collections.reverseOrder())
								.map(String::valueOf)
								.collect(Collectors.joining(" "));
		
		System.out.println(output);
	}
	
	public static int[] input() {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		
		int arr[] = new int[N];
		
		for(int num = 0; num < N; num++)
			arr[num] = sc.nextInt();
		
		return arr;
	}

}
