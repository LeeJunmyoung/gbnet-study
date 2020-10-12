import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 
 * N 명의 학생 정보가 있다. 학생 정보는 학생의 이름과 학생의 성적으로 구분된다. 
 * 각 학생의 이름과 성적정보가 주어졌을 때 성적이 낮은 순서대로 학생의 이름을 출력하는 프로그램을 작성하시오.
 * 
 * - 입력조건
 * 첫 번째 줄에 학생의 수 N이 입력된다 1 <= N <= 100,000
 * 두 번째 줄부터 N+1 번째 줄에는 학생의 이름을 나타내는 문자열 A와 학생의 성적을 나타내는 정수 B가 공백으로 구분되어 입력된다.
 * 
 * - 출력조건
 * 모든 학생의 이름을 성적이 낮은 순서대로 출력하고 동점일 경우 이름의 내림차순으로 정렬한다.
 * 
 * ex)
 * input
 * 3
 * 홍길동 95
 * 이순신 77
 * 이직 95
 * 
 * output
 * 홍길동 이직 이순신
 *
 */

public class ScoreTest {

	public static void main(String[] args) {
		List<Student> input = input();
		
		input.sort(Comparator.comparing(Student::getScore).thenComparing(Student::getName).reversed());
		
		System.out.println(input.stream().map(Student::getName).collect(Collectors.joining(" ")));
	}
	
	public static List<Student> input(){
		List<Student> list = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		
		int N = Integer.parseInt(sc.nextLine());
		
		for(int num = 0; num < N; num++) {
			String line = sc.nextLine();
			
			String name	= line.split(" ")[0];
			int score	= Integer.parseInt(line.split(" ")[1]);
			
			Student student = new Student(name, score);
			
			list.add(student);
		}
		
		return list;
	}

	
	
}

class Student {
	private String	name;
	private int		score;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Student(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	
}
