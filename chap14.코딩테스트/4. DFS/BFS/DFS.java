/**
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
public class DFS {

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
		
		dfs(graph, 1, visited);
	}
	
	public static void dfs(int[][] graph, int v, boolean[] visited) {
		if(visited[v] == true)
			return;
		
		visited[v] = true;
		System.out.print(v + " ");
		
		for(int num = 0; num < graph[v].length; num++) {
			dfs(graph, graph[v][num], visited);
		}
	}
}
