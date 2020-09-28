/**
 * 
 * 캐릭터가 맵 안에서 움직이는 시스템을 개발 중이다.
 * 캐릭터가 있는 장소는 1 * 1 크기의 정사각형으로 이뤄진 N * M 크기의 직사각형으로, 각각의 칸은 육지 또는 바다이다.
 * 캐릭터는 동서남북 중 한 곳을 바라본다.
 * 맵의 각 칸은 (A, B)로 나타 낼 수 있고, A는 북쪽으로부터 떨어진 개수, B는 서쪽으로 부터 떨어진 개수이다.
 * 캐릭터는 동서남북으로 이동할수 있고, 바다로 되어 있는 공간은 갈수 없다.
 * 1. 현재 위치에서 현재방향을 기준으로 왼쪽 방향(반시계방향으로 90도 회전한 방향) 부터 차례대로 갈곳을 정한다.
 * 2. 캐릭터의 바로 왼쪽 방향에 가보지 않은 칸이 있다면 왼쪽방향으로 회전한 다음 왼쪽으로 1칸을 전진한다. 
 * 3. 왼쪽방향에 가보지 않은 칸이 없다면 회전만 수행하고 1단계로 돌아간다.
 * 4. 만약 네방향 모두 이미 가본칸이거나 바다로 되어 있는 칸인 경우에는, 바라보는 방향을 유지한 채로 한칸 뒤로 가고 1단꼐로 돌아간다.
 * 단, 이때 뒤쪽방향이 바다인 칸이라 뒤로 갈 수없는 경우 움직임을 멈춘다. 
 * 
 * 3 <= N,M <= 50
 * 캐릭터가 바라보는 값은 4개 존재한다. 0:북족 , 1:동쪽, 2: 남쪽, 3:서쪽
 * 육지 : 0 , 바다 : 1
 * 맵의 외곽은 항상 바다이다.
 * 
 * - map 이 
 * 1 1 1
 * 1 0 0
 * 1 1 0
 * 의경우 ArrayIndexOutOfBoundsException 가 발생하는데
 * 위에 조건 자체가 맵의 외곽은 항상 바다임을 생각하며
 * 예외처리에 대해서는 고려하지 않고 빠르게 문제를 풀어나간다.
 */
public class MapMoveTest {

	public static void main(String[] args) {
		int N = 4, M= 4;
		int [][]map = {
						{1,1,1,1}
					   ,{1,0,0,1}
					   ,{1,1,0,1}
					   ,{1,1,1,1}
					   };
		int A = 1, B = 1, d = 0;
		System.out.println(getMoveCnt(N, M, map, A, B, d));
	}
	
	public static int getMoveCnt(int N, int M, int[][] map, int A, int B, int d) {
		boolean visited[][] = new boolean[N][M]; // 방문 체크 배열
		
		visited[A][B] = true; // 시작 위치 방문체크
		int cnt = 1; // 방문 한 타일 개수
		
		int x = A;
		int y = B;
		// 방향 별 이동할 값 
		int moveX[] = {0, 1, 0, -1};
		int moveY[] = {1, 0, -1, 0};
		
		int direction	= d;
		int turn		= 0;
		while(true) {
			direction = leftSideView(direction);
			
			int dx = x + moveX[direction];
			int dy = y + moveY[direction];
			
			if(map[dx][dy] == 0 && visited[dx][dy] != true) {
				visited[dx][dy] = true;
				x = dx;
				y = dy;
				turn = 0;
				cnt++;
			}
			else {
				turn++;
			}
			
			if(turn == 4) {
				dx = x - moveX[direction];
				dy = y - moveY[direction];
				if(map[dx][dy] == 0) {
					x = dx;
					y = dy;
					turn = 0;
				}else {
					break;
				}
			}
		}
		
		return cnt;
	}
	
	// 좌측을 바라보는 기능
	public static int leftSideView(int d) {
		return (d + 1) % 4;
	}
}
