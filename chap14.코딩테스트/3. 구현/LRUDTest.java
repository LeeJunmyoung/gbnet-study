/**
 * 
 * 여행가 A는 N * N 크기의 정사각형 공간 위에 서 있다.
 * 이 공간은 1 * 1 크기의 정사각형으로 나누어져 있다. 가장 큰 위 좌표는 (1,1) 이며, 가장 오른쪽 아래의 좌표는 (N,N)이다.
 * 여행가 A는 상,하,좌,우 방향으로 이동할 수 있으며, 시작 좌표는 항상(1,1)이다.
 * 여행가 A에 대한 계획서가 있고, 하나의 줄에 띄어쓰기를 기준으로 하여 L,R,U,D 중 하나의 문자가 반복적으로 적혀있다.
 * 여행가의 목적지는?
 * 이때 여행가가 공간을 벗어나는 움직임은 무시된다.
 * input
 * 5
 * R R R U D D
 * (1,2) -> (1,3) -> (1,4) -> X -> (2,4) -> (3,4)
 * output
 * 3 4
 */
public class LRUDTest {

	public static void main(String[] args) {
		int N = 5;
		String move ="R R R U D D";
		System.out.println(travel(N, move));
	}
	
	public static String travel(int N, String move) {
		String[] moveArr = move.split(" ");
		
		int x = 1;
		int y = 1;
		
		for(String command : moveArr) {
			x = moveX(N, x, command);
			y = moveY(N, y, command);
		}
		return x + " " + y;
	}
	
	public static int moveX(int N,int x, String command) {
		int answer = x;
		switch (command) {
			case "U":
				answer--;
				break;
			case "D":
				answer++;
				break;
			default:
				break;
		}
		return answer > N || answer < 1 ? x : answer;
	}
	
	public static int moveY(int N,int y, String command) {
		int answer = y;
		switch (command) {
			case "R":
				answer++;
				break;
			case "L":
				answer--;
				break;
			default:
				break;
		}
		return answer > N || answer < 1 ? y : answer;
	}

}
