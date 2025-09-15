import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * N개인 노드에서 시작위치 X에서 각 노드의 가장 가까운 거리를 구해야한다.
 * 첫 번째 줄 입력으로는 N개의 노드와 M개의 간선을 각 각 입력 받는다.
 * 두 번째 줄 입력으로는 시작 노드의 위치를 입력받는다.
 * 세 번째 줄 부터는 노드의 위치, 연결된 노드, 간선의 거리를 입력받게 된다. (단, 방향은 노드의 위치 에서 연결된 노드 방향으로만 움직일 수 있다.)
 * 
 * 출력으로 해당 노드로 갈수 없다면 -1로 표기한다.
 * X노드의 경우 0으로 표기한다.
 * 각 노드로 갈 수 있는 최단거리를 구한다.
 * 
	input
	6 11	// 6개의 노드가 존재하며, 연결된 간선의 숫자는 11개 이다.
	1		// 1번노드에서 시작한다.
	1 2 2	// 1번 노드에서 2번노드방향으로 거리는 2이다.
	1 3 5
	1 4 1
	2 3 3
	2 4 2
	3 2 3
	3 6 5
	4 3 3
	4 5 1
	5 3 1
	5 6 2
	
	output
	0
	2
	3
	1
	2
	4
 * 
 */
public class FloydWarshallTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt(); // 노드의 개수
		int M = sc.nextInt(); // 간선의 개수
		int X = sc.nextInt(); // 시작 위치
		
		// N * N 배열 생성 N번째 배열에서 N번째로 가는 그래프
		int graph[][] = new int[N + 1][N + 1];
		
		// 배열 최고 값으로 초기화
		
		for(int num = 0; num < graph.length; num++) {
			Arrays.fill(graph[num], Integer.MAX_VALUE);
			graph[num][num] = 0;
		}
		
		// 모든 간선 정보를 입력받기
		for (int i = 0; i < M; i++) {
			int start		= sc.nextInt();
			int destination	= sc.nextInt();
			int distance	= sc.nextInt();
			
			graph[start][destination] = distance;
		}
		
		int[][] distanceGraph = floydWarshall(graph);
		
		for(int num = 1; num < N + 1; num++) {
			System.out.println(distanceGraph[X][num]);
		}
	}
	
	public static int[][] floydWarshall(int[][] graph) {
		int[][] distanceGraph = graph.clone(); // 거리를 저장할 배열 복사
		
		int graphSize = graph.length;
		
		// 경유노드
		for(int x = 0; x < graphSize; x++) {
			// 출발 노드
			for(int y = 0; y < graphSize; y++) {
				// 도착 노드
				for(int z = 0; z < graphSize; z++) {
					if((long)distanceGraph[y][z] > (long)distanceGraph[y][x] + (long)distanceGraph[x][z]) {
						distanceGraph[y][z] = distanceGraph[y][x] + distanceGraph[x][z];
					}
				}
			}
		}
		
		return distanceGraph;
	}

}
