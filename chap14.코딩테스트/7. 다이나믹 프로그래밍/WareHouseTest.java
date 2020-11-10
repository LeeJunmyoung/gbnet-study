/**
 * 
 * N 개의 일직선상의 창고가 있다. 각 창고에는 K개의 식량이 있으며,
 * 식량을 꺼낼때 인접한 창고에서는 식량을 꺼낼수 없다.
 * 예를 들어 3번째 창고에서 식량을 꺼냈다면 2,4번째 창고에서는 식량을 꺼낼수 없다.
 * N개의 창고에 식량이 주어 졌을때 가장 많이 꺼낼수 있는 식량의 개수는 무엇인가?
 * 
 * input
 * 1 3 1 5
 * 
 * output
 * 8 (2번째 4번째 창고에서 꺼낸 값)
 * 
 */
public class WareHouseTest {

	public static void main(String[] args) {
		int[] wareHouseFoods = {1, 3, 1, 5};
		System.out.println(getFood(wareHouseFoods));
	}
	
	public static int getFood(int[] wareHouseFoods) {
		int length = wareHouseFoods.length;
		
		int dp[] = new int[length];
		
		// 1, 2번째 초기화
		dp[0] = wareHouseFoods[0];
		dp[1] = Math.max(dp[0], wareHouseFoods[1]);
		
		for(int num = 2; num < dp.length; num++) {
			dp[num] = 0; // 초기화;
			
			dp[num] = Math.max(dp[num - 1], dp[num - 2] + wareHouseFoods[num]);
			// sum(n - 2번째) + n 이 더큰지 , sum(n-1번째)까지 더 큰지 비교후
			// dp[num]에 값을 집어넣는다 dp[num]의 값은 num 번째 에서 가장 많이 꺼낼수 있는 식량의 개수다.
		}
		
		return dp[length - 1];
	}

}
