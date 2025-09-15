import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * N * M 크기의 미로가 있다.
 * 미로의 시작위치는 (1,1) 이고 미로의 출구는 (N,M)의 위치에 존재하며 한번의 한 칸씩 이동할 수 있다.
 * 이때 벽 부분은 0으로, 도로 부분은 1로 표시되어 있다.
 * 미로는 반드시 탈출 할 수 있는 형태로 제시된다. 이때 탈출하기 위해 움직여야하는 최소칸의 개수를 구하시오.
 * 시작칸과 마지막 칸을 모두 폼해서 계산해야한다.
 * 
 * 4 <= N, M <= 200
 * 
 * - input
 * 101010
 * 111111
 * 000001
 * 111111
 * 111111
 * 
 * - output
 * 10
 * 
 */

public class MazeBFSTest {

	public static void main(String[] args) {
		int maze[][] = {
			{1,0,1,0,1,0}
			,{1,1,1,1,1,1}
			,{0,0,0,0,0,1}
			,{1,1,1,1,1,1}
			,{1,1,1,1,1,1}
		};
		
		System.out.println(moveCnt(maze));
		
		for(int[] x : maze) {
			for(int y : x) {
				System.out.print(y + "\t");
			}
			System.out.println();
		}
	}
	
	public static int moveCnt(int[][] maze) {
		int answer = 0;
		
		// 위쪽부터 시계방향으로
		int[] dx = {0, 1, 0, -1};
		int[] dy = {1, 0, -1, 0};
		
		// x,y 좌표 큐에 저장
		Queue<Integer> xQ = new LinkedList<Integer>();
		Queue<Integer> yQ = new LinkedList<Integer>();
		
		// 시작지점 저장
		xQ.add(0);
		yQ.add(0);
		
		while(xQ.isEmpty() == false) {
			int x = xQ.poll();
			int y = yQ.poll();
			
			for(int num = 0; num < 4; num++) {
				int moveX = x + dx[num];
				int moveY = y + dy[num];
				if(moveX < 0 || moveX >= maze.length || moveY < 0 || moveY >= maze[moveX].length) {
					continue;
				}
				else if(moveX == 0 && moveY == 0) {
					continue;
				}
				else if(maze[moveX][moveY] == 0) {
					continue;
				}
				else if(maze[moveX][moveY] == 1) {
					maze[moveX][moveY] = maze[x][y] + 1;
					xQ.add(moveX);
					yQ.add(moveY);
					
					// 전체 경로에 대한 값을 보고 싶으면 주석처리 
					if(moveX == maze.length - 1 && moveY == maze[0].length - 1) {
						xQ.clear();
						yQ.clear();
						break;
					}
				}
			}
		}
		
		answer = maze[maze.length - 1][maze[0].length - 1];
		
		return answer;
	}
	
}
