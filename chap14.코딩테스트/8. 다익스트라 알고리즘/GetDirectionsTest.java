import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * 1번부터 N번까즤의 도시가 존재하는데 특정 도시끼리는 서로 도로를 통해 연결되어 있다. 
 * 회사원 A는 1번 도시에 있으면 X번 도시에 들러 물건을 구매후 Y번 도시로 갈 예정이다
 * 각 도로를 이동하는데 1시간이 소모된다고 했을때 가장 적게 소모되는 시간은?
 * 
 * - 입력 조건
 * 1. 첫번째 줄에 전체 도시의 개수 N과 경로의 개수 M개가 주어진다. 
 * 2. 둘째 줄 부터 M + 1번째 줄에는 열결된 두 도시의 번호가 주어진다.  
 * 3. M+2번째 줄에는 X(경유 도시), Y(도착도시) 가 주어진다. 
 * 4. 해당 줄의 값은 공백으로 구분된다.
 * 
 * - 출력 조건
 * 첫째 줄에 회사원 A가 X번 도시를 경유해 Y번 도시에 도착하는 최소 이동시간을 출력한다.
 * 만약 X번 회사에 도달할수 없을시 -1을 출력한다.

input 
5 7
1 2
1 3
1 4
2 4
3 4
3 5
4 5
5 4

output
3

 */
public class GetDirectionsTest {
	
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		
		int graph[][] = setGraph();
		
		int X		= sc.nextInt();
		int Y		= sc.nextInt();
		
		int[][] distanceGraph = floydWarshall(graph);
		
		printDistance(X, Y, distanceGraph);
		
		sc.close();
	}
	
	public static int[][] setGraph() {
		int N = sc.nextInt(); // 도시의 개수
		int M = sc.nextInt(); // 간선의 개수
		
		// N * N 배열 생성 N번째 배열에서 N번째로 도시로 가는 간선 그래프 배열
		int graph[][] = new int[N + 1][N + 1];
		
		// 배열 최고 값으로 초기화
		for(int num = 1; num < graph.length; num++) {
			Arrays.fill(graph[num], Integer.MAX_VALUE); // 초기화
			graph[num][num] = 0; // 본인의 도시는 이동시간 0 이다.
		}
		
		// 모든 간선 정보를 입력받기
		for (int i = 0; i < M; i++) {
			int cityA		= sc.nextInt();
			int cityB		= sc.nextInt();
			
			// 서로 지나갈수 있으므로. 대칭되는 그래프가 완성됨.
			graph[cityA][cityB] = 1;
			graph[cityB][cityA] = 1;
		}
		
		return graph;
	}
	
	public static int[][] floydWarshall(int[][] graph) {
		int[][] distanceGraph = graph.clone(); // 거리를 저장할 배열 복사
		
		int graphLength = graph.length;
		
		for(int z = 1; z < graphLength; z++) { // 경유 
			for(int s = 1; s < graphLength; s++) { // 시작 지점
				for(int e = 1; e < graphLength; e++) { // 도착지점
					if((long)distanceGraph[s][e] > (long)distanceGraph[s][z] + (long)distanceGraph[z][e]) {
						distanceGraph[s][e] = distanceGraph[s][z] + distanceGraph[z][e];
					}
				}
			}
		}
		
		return distanceGraph;
	}
	
	public static int getDistanceCity(int startCity, int endCity, int[][] distanceGraph) {
		return distanceGraph[startCity][endCity];
	}
	
	public static void printDistance(int X, int Y, int[][] distanceGraph) {
		int result;
		long distance = (long)getDistanceCity(1, X, distanceGraph) + (long)getDistanceCity(X, Y, distanceGraph);
		
		if(distance > Integer.MAX_VALUE) {
			result = -1;
		}
		else {
			result = (int) distance;
		}
		
		System.out.println(result);
	}
	

}
