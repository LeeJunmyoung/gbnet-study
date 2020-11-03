package binarySort;

import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * 떡볶이 집에서 떡을 만드는 날이다. 떡볶이 떡은 길이가 일정하지 않고, 대신 포장되는 떡은 절단기로 잘라서 맞춰준다.
 * 절단기의 높이 (H)로 정하여 줄지어진 떡을 한 번에 절단한다. 높이가 H보다 긴 떡은 H 위의 부분이 잘릴 것이고, 작은 떡은 잘리지 않는다.
 * 예를 들어 높이가 19, 14, 10, 15cm 인 떡을 절단기 15 인 절단기로 자르면, 15, 14, 10, 15가 될 것이다.
 * 잘린 떡의 길이는  차례대로 4, 0, 0, 0 이다. 남은 떡은 4 가 된다.
 * 
 * 남는 떡이 M 이어야 할 때, 적어도 M 만큼 떡이 남게 하기 위해 절단기에 설정 할 수 있는 최대값을 구하시오.
 * 
 * 남는 떡의 길이 M이 주어진다. (1 <= N <= 1,000,000 , 1 <= M <= 2,000,000,000)
 * N개의 떡의 개별 높이가 주어딘다. 
 * 
 *
 */

public class RiceCakeTest {

	public static void main(String[] args) {
		int[] riceCakesHeights = {19, 15, 10, 17};
		System.out.println(getCuttingHeight(6, riceCakesHeights));
		
		int[] riceCakesHeights2 = {19, 14, 10, 15};
		System.out.println(getCuttingHeight(4, riceCakesHeights2));
	}
	
	public static int getCuttingHeight(int M, int[] riceCakesHeights) {
		int cuttingHeight = 0;
		
		
		int riceCakeMaxHeight = Collections.max(Arrays.asList(Arrays.stream(riceCakesHeights).boxed().toArray(Integer[]::new))); // 떡의 최대높이
		
		
		
		int start	= 0;
		int end		= riceCakeMaxHeight;
		int mid		= 0;
		
		int beforeSum = Integer.MAX_VALUE;
		
		while(start < end) {
			mid = (start + end) / 2;
			
			int sum = 0;
			
			for(int riceCakesHeight : riceCakesHeights) {
				if(riceCakesHeight > mid) {
					sum += riceCakesHeight - mid;
				}
			}
			
			System.out.println(String.format("start : %d, end : %d, mid : %d, sum : %d", start, end, mid, sum));
			
			if(sum < M) {
				end = mid;
			}
			else if(sum == M){
				cuttingHeight = mid;
				break;
			}
			else {
				if(beforeSum > sum) {
					beforeSum = sum;
					start = mid;
				}
				else {
					
					cuttingHeight = mid;
					break;
				}
				
			}
		}
		
		return cuttingHeight;
	}

}
