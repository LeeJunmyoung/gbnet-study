import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 
 * A와 B 배열 두개가 있다. 두 배열은 N개의 원소로 구성되어 있으며 배열의 원소는 모두 자연수이다.
 * 최대 K번의 바꿔치기 연산을 수행 할 수 있는데, 바꿔치기 연산이란 배열 A에 있는 원소 하나와 배열 B에 있는 
 * 원소 하나를 골라서 두 원소를 서로 바꾸는 것을 말한다. 
 * 그래서 A의 모든 원소의 합이 최대가 되도록하는 것이 목표이다.
 * 
 * 배열 A = [1,2,5,4,3]
 * 배열 B = [5,5,6,6,5]
 * 일때 K = 2 일경우
 * 
 * 배열 A = [6,6,5,4,3] 이 되어 24 가 출력되어야한다.
 * 
 * - 입력조건
 * 첫 번째 줄에 N, K가 공백으로 구분되어 입력된다. 1<=N<=100,000  0<=K<=N
 * 두번째 줄에 공백으로 구분되는 배열 A의 원소들이 입력된다.
 * 세번째 줄에 공백으로 구분되는 배열 B의 원소들이 입력된다.
 * 
 * - 출력조건
 * 최대 K번의 바꿔치기 연산을 수행하여 만들 수 있는 배열 A의 모든 원소의 합의 최대값을 출력한다
 * 
 * ex)
 * input
 * 5 2
 * 1 2 5 4 3
 * 5 5 6 6 5
 * 
 * output
 * 24
 *
 */

public class ArraySwapTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] arr = input();
		
		int[] A = arr[0];
		int[] B = arr[1];
		
		Arrays.sort(A);
		
		Integer[] BB = Arrays.stream(B).boxed().toArray(Integer[]::new);
		Arrays.sort(BB, Collections.reverseOrder());
		
		for(int num = 0; num < K; num++) {
			if(A[num] > BB[num])
				break;
			else 
				A[num] = BB[num];
		}
		
		System.out.print(Arrays.stream(A).sum());
	}
	
	static int K = 0;
	
	public static int[][] input(){
		
		Scanner sc = new Scanner(System.in);
		
		String line = sc.nextLine();
		
		int N	= Integer.parseInt(line.split(" ")[0]);
		K		= Integer.parseInt(line.split(" ")[1]);
		
		int arr[][] = new int[2][N];
		
		String A = sc.nextLine();
		String B = sc.nextLine();
		
		arr[0] = Arrays.stream(A.split(" ")).mapToInt(Integer::parseInt).toArray();
		arr[1] = Arrays.stream(B.split(" ")).mapToInt(Integer::parseInt).toArray();
		
		sc.close();
		
		return arr;
	}

}
