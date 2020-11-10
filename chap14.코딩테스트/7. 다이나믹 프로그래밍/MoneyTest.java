/**
 * 
 * N개의 서로 다른 액수의 화폐가 있다. 이 화폐를 이용하여
 * 주어진 액수 M을 만들기 위한 최소한의 화폐개수를 구하는 문제이다.
 * 
 * 예를 들어, 2원과 3원 단위의 화폐가 있을 시  10원을 만들기 위한 최소한의 개수는
 * 3원 2개, 2원 2개 이다.
 *
 */
public class MoneyTest {

	public static void main(String[] args) {
		int[] coins = {2, 3};
		System.out.println(getMoneyCnt(coins, 10));
	}
	
	public static int getMoneyCnt(int[] coins, int M) {
		int dp[] = new int[M + 1];
		
		dp[0] = 0;
		for(int num = 1; num <= M; num++) {
			dp[num] = M;
		}
		
		for(int num = 0; num <= M; num++) {
			for(int coin : coins) {
				if(num >= coin) {
					dp[num] = Math.min(dp[num], dp[num - coin] + 1);
				}
			}
		}
		
		return dp[M] == 0 ? -1 : dp[M];
	}
}
