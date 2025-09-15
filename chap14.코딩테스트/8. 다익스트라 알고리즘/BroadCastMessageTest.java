import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * N 개의 도시가 있다. 각 도시에 메시지를 보내기 위해서는 해당 구간에 통로가 설치되어야 한다.
 * 통로는 일방향이며, 해당 통로를 통해 메시지를 전달할 경우 일정 시간이 소요된다.
 * A라는 도시에서 메시지를 보낼경우 최대한 많은 도시로 메시지를 보내고자 할 때,
 * 메시지를 받을 수 있는 최대 도시의 개수와 모두 메시지를 받는 데까지 걸리는 시간은 얼마인가?
 * 
 * - 입력조건
 * 1. 첫째 줄에 도시의 개수N, 통로의 개수 M 메시지를 보내는 도시 A가 주어진다 (1 <= N <= 30,000 | 1 <= M <= 200,000 | 1 <= A <= N)
 * 2. 둘째 줄부터 M + 1 번째 줄에 걸쳐서 통로에 대한 정보 X, Y, Z가 주어진다. 이는 특정 도시 X에서 다른 도시 Y로 이어지는 통로가 있으며, 전달되는데 소요되는 시간 Z를 의미한다.
 * 
 * - 출력조건
 * 1. 첫째 줄에 도시 A에서 보낸 메시지를 받는 도시의 총 개수와 총 걸리는 시간을 공백으로 구분하여 출력한다.
 */

public class BroadCastMessageTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt(); // 도시의 개수
		int M = sc.nextInt(); // 통로의 개수
		int A = sc.nextInt(); // 시작 도시
		
		CityWires cityWires = new CityWires(N); // 연결된 간선들의 집합
		
		// 모든 간선 정보를 입력받기
		for (int i = 0; i < M; i++) {
			int X	= sc.nextInt(); // 시작지점
			int Y	= sc.nextInt(); // 도착지점
			int Z	= sc.nextInt(); // 전달되는데 소요되는 시간
			
			Wire wire = new Wire(X, Y, Z);
			cityWires.getCityWire(X).add(wire);
		}
		
		int[] costArr = broadCastMessage(cityWires, A);
		
		printCityAndCost(costArr);
	}
	
	public static int[] broadCastMessage(CityWires cityWires, int A) {
		int[] costArr = new int[cityWires.getCityWiresListSize() + 1]; // 각 노드의 가장 가까운 거리
		Arrays.fill(costArr, Integer.MAX_VALUE); // Integer.MAX_VALUE 로 초기화
		
		Queue<Wire> q = new LinkedList<>();
		costArr[A] = 0;
		q.offer(new Wire(A, A, 0));
		
		while(q.isEmpty() == false) {
			Wire wire = q.poll();
			
			int destCity	= wire.getDestCity();
			int cost		= wire.getCost();
			
			if(costArr[destCity] < cost) continue;
			
			for(int i = 0; i < cityWires.getCityWire(destCity).size(); i++) {
				int nextStartCity	= cityWires.getCityWire(destCity).get(i).getStartCity();
				int nextDestCity	= cityWires.getCityWire(destCity).get(i).getDestCity();
				int nextCityCost	= costArr[destCity] + cityWires.getCityWire(destCity).get(i).getCost();
				
				if(nextCityCost < costArr[nextDestCity]) {
					costArr[nextDestCity] = nextCityCost;
					q.offer(new Wire(nextStartCity, nextDestCity, nextCityCost));
				}
			}
		}
		
		return costArr;
	}
	
	public static void printCityAndCost(int[] costArr) {
		int receiveCityCnt	= -1;
		int maxCost			= 0;
		for(int cost : costArr) {
			if(cost < Integer.MAX_VALUE) {
				receiveCityCnt ++;
				
				if(cost > maxCost) maxCost = cost;
			}
		}
		
		System.out.println(receiveCityCnt + " " + maxCost);
	}

}

class Wire{
	
	private int startCity;
	private int destCity;
	private int cost;
	
	public Wire(int startCity, int destCity, int cost) {
		this.startCity = startCity;
		this.destCity = destCity;
		this.cost = cost;
	}

	public int getStartCity() {
		return startCity;
	}

	public void setStartCity(int startCity) {
		this.startCity = startCity;
	}

	public int getDestCity() {
		return destCity;
	}

	public void setDestCity(int destCity) {
		this.destCity = destCity;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}

class CityWires {
	
	private List<List<Wire>> cityWiresList = new ArrayList<>();
	
	public CityWires(int N) {
		for(int num = 0; num < N + 1; num++) {
			List<Wire> wireList = new ArrayList<>();
			cityWiresList.add(wireList);
		}
	}
	
	public List<Wire> getCityWire(int city) {
		return cityWiresList.get(city);
	}
	
	public int getCityWiresListSize() {
		return cityWiresList.size();
	}
}
