/**
 * N * M 크기의 얼음틀이 있다.
 * 구멍이 뚫려있는 부분을 0, 칸막이가 존재하는 부분은 1로 표시된다.
 * 구멍이 뚫려 있는 부분끼리 상,하,좌,우로 붙어 있는 경우 서로 연결 되어 있는 것으로 간주한다.
 * 이때 얼음 틀의 모양이 주어졌을 때 생성되는 총 아이스트림의 개수를 구하는 프로그램을 작성하시오.
 * 
 * 입력
 * 00110
 * 00011
 * 11111
 * 00000
 * 
 * 출력
 * 3
 * 
 */

public class JuiceIceDFSTest {

	public static void main(String[] args) {
		int[][] iceFrame = {
							{0,0,1,1,0}
							, {0,0,0,1,1}
							, {1,1,1,1,1}
							, {0,0,0,0,0}
						};
		
		System.out.println(getIceCnt(iceFrame));
		
		int[][] iceFrame1 = {
							{0,0,1}
							,{0,1,1}
							,{1,0,1}
						};
		
		System.out.println(getIceCnt(iceFrame1));
	}
	
	public static boolean dfs(int[][] iceFrame, int x, int y) {
		if(x < 0 || x >= iceFrame.length || y < 0 || y >= iceFrame[x].length) {
			return false;
		}
		
		if(iceFrame[x][y] == 0) {
			iceFrame[x][y] = 1;
			
			dfs(iceFrame, x + 1, 	y		);
			dfs(iceFrame, x, 		y - 1	);
			dfs(iceFrame, x - 1,  	y		);
			dfs(iceFrame, x, 		y + 1	);
			
			return true;
		}
		
		return false;
	}
	
	public static int getIceCnt(int[][] iceFrame) {
		int answer = 0;
		for(int x = 0; x < iceFrame.length; x++) {
			for(int y = 0; y< iceFrame[x].length; y++) {
				if(dfs(iceFrame, x, y)) {
					answer++;
				}
			}
		}
		
		return answer;
	}
}
