/**
 * 
 * 체스판과 같은 8(a~h) * 8(1~8)  좌표평면에서 나이트는 아래와 같이 L자 형태로만 이동 할 수 있으며, 정원 밖으로는 나갈 수 없다. 
 * 나이트는 특정한 위치에서 다음과 같은 2가지 경우로 이동할 수 있다.
 * 
 * 1. 수평으로 두칸 이동한 뒤 수직을 한칸 이동.
 * 2. 수직으로 두칸 이동한 뒤 수평으로 한칸 이동.
 * 
 * 예를 들어 나이트의 위치가 a1 일경우 이동 할 수 있는 경우의 수는 2가지이다.
 * 1. 오른쪽으로 두칸 이동 후 아래로 한칸 이동(c2)
 * 2. 아래로 두칸 이동 후 오른쪽으로 한칸 이동(b3)
 * 
 */
public class KnightTest {

	public static void main(String[] args) {
		String location = "a1";
		System.out.println(knightMoveCnt(location));
	}
	
	public static int knightMoveCnt(String location) {
		int cnt = 0;
		
		int[][] steps = {{-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};
		
		int x = (location.charAt(0) - 'a') + 1;
		int y = (location.charAt(1) - '1') + 1;
	
		for(int num = 0; num < steps.length; num++) {
			int moveX = steps[num][0];
			int moveY = steps[num][1];
			
			if( (x + moveX > 0 && x + moveX < 9)
				&&
				(y + moveY >0 && y + moveY < 9)
			) {
				cnt++;
			}
		}
		return cnt;
	}

}
