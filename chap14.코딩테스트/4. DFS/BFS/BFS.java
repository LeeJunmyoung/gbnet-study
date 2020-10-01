import java.util.LinkedList;
import java.util.Queue;

/**
 * BFS 넓이 우선 탐색
 * 
 * 인접 리스트
 *  4 - 3 - 1 - 2 
 * 	  \ |	|	|
 * 		5	8 - 7 - 6
 * 
 * 인접 행렬
 * {
 * 	{}			// 0번째
 * 	, {2, 3, 8}	// 1번째
 * 	, {1, 7}
 *  , {1, 4, 5}
 *  , {3, 5}
 *  , {3, 4}
 *  , {7}
 *  , {2, 6, 8}
 *  , {1, 7}	// 8번째
 * }
 * 
 * 일때 모든 노드의 탐색
 */
public class BFS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] graph ={
			  	{}			// 0번째
			  , {2, 3, 8}	// 1번째
			  , {1, 7}
			  , {1, 4, 5}
			  , {3, 5}
			  , {3, 4}
			  , {7}
			  , {2, 6, 8}
			  , {1, 7}	// 8번째
		};
		boolean[] visited = new boolean[graph.length];
		
		bfs(graph, 1, visited);
	}
	
	static Queue<Integer> queue = new LinkedList<Integer>();
	
	public static void bfs(int[][] graph, int v, boolean[] visited) {
		queue.add(v);
		visited[v] = true;
		
		
		while(queue.isEmpty() == false) {
			int idx = queue.poll();
			System.out.print(idx + " ");
			for(int num = 0; num < graph[idx].length; num++) {
				if(visited[graph[idx][num]] == false) {
					visited[graph[idx][num]] = true;
					queue.add(graph[idx][num]);
				}
			}
		}
	}
}
