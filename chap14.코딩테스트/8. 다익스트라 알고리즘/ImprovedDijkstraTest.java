import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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
public class ImprovedDijkstraTest {

	// 각 연결된 노드의 정보를 저장함.
	static ArrayList<ArrayList<Node>> graph = new ArrayList<ArrayList<Node>>(); 
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt(); // 노드의 개수
		int M = sc.nextInt(); // 간선의 개수
		int X = sc.nextInt(); // 시작 위치
		
		// 그래프 초기화
		for (int i = 0; i <= N; i++) {
			graph.add(new ArrayList<Node>());
		}
		
		// 모든 간선 정보를 입력받기
		for (int i = 0; i < M; i++) {
			int start		= sc.nextInt();
			int destination	= sc.nextInt();
			int distance	= sc.nextInt();

			//start 노드에서 destination 노드로 가는 비용 : distance
			graph.get(start).add(new Node(destination, distance));
		}
		
		int[] distanceArr = new int[N + 1]; // 각 노드의 가장 가까운 거리
		
		Arrays.fill(distanceArr, Integer.MAX_VALUE); // Integer.MAX_VALUE 로 초기화
		
		dijstra(X, distanceArr);
		
		Arrays.stream(distanceArr, 1, N + 1).forEach(System.out::println);
	}
	
	public static void dijstra(int start, int[] distanceArr) {
		Queue<Node> q = new LinkedList<Node>();
		
		// 시작지점 0으로 초기화
		q.offer(new Node(start, 0));
		distanceArr[start] = 0;
		
		while(q.isEmpty() == false) {
			Node node = q.poll();
			
			int destination	= node.getDestination(); 
			int distance	= node.getDistance();
			
			if(distanceArr[destination] < distance) continue;
			
			for(int i = 0; i < graph.get(destination).size(); i++) {
				int cost = distanceArr[destination] + graph.get(destination).get(i).getDistance();
				
				if(cost < distanceArr[graph.get(destination).get(i).getDestination()]) {
					distanceArr[graph.get(destination).get(i).getDestination()] = cost;
					q.offer(new Node(graph.get(destination).get(i).getDestination(), cost));
				}
			}
		}
	}

}

class Node {
	private int destination; // 목적지
	
	private int distance; // 거리
	
	public Node(int destination, int distance) {
		super();
		this.destination = destination;
		this.distance = distance;
	}

	public int getDestination() {
		return destination;
	}

	public int getDistance() {
		return distance;
	}
}

