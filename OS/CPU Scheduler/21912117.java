import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HW2 {
	public static void FCFS(String fileName) throws IOException {
		System.out.println("FCFS");
		LinkedList<Process> arr = fileIO(fileName);
		LinkedList<Process> bur = new LinkedList<Process>();
		LinkedList<Result> result = new LinkedList<Result>();
		ArriveSort ar = new ArriveSort();
		Process t, s;
		int totalTime = 0, turnaround, waitingTime;

		Collections.sort(arr, ar);
		while (!arr.isEmpty()) {
			turnaround = 0;
			waitingTime = 0;
			for (int i = 0; i < arr.size(); i++) {
				s = arr.get(i);
				if (totalTime >= s.arriveTime) {
					bur.add(new Process(s.id, s.arriveTime, s.burstTime, s.priority));
					arr.remove(i);
					i--;
				}
			}

			if (!bur.isEmpty()) {
				t = bur.getFirst();
				totalTime += t.burstTime;
				turnaround = totalTime - t.arriveTime;
				waitingTime = turnaround - t.burstTime;
				result.add(new Result(t.id, t.burstTime, waitingTime));
				bur.removeFirst();
			} else
				totalTime++;
		}
		while (!bur.isEmpty()) {
			t = bur.getFirst();
			totalTime += t.burstTime;
			turnaround = totalTime - t.arriveTime;
			waitingTime = turnaround - t.burstTime;
			result.add(new Result(t.id, t.burstTime, waitingTime));
			bur.removeFirst();
		}

		prt(result, totalTime);
	}

	public static void SJF(String fileName) throws IOException {
		System.out.println("SJF");
		LinkedList<Process> arr = fileIO(fileName);
		LinkedList<Process> bur = new LinkedList<Process>();
		LinkedList<Result> result = new LinkedList<Result>();
		ArriveSort ar = new ArriveSort();
		BurstSort bu = new BurstSort();
		Process s, t;
		int totalTime = 0, turnaround, waitingTime;

		Collections.sort(arr, ar);

		while (!arr.isEmpty()) {
			turnaround = 0;
			waitingTime = 0;
			for (int i = 0; i < arr.size(); i++) {
				s = arr.get(i);
				if (totalTime >= s.arriveTime) {
					bur.add(new Process(s.id, s.arriveTime, s.burstTime, s.priority));
					arr.remove(i);
					i--;
				}
			}

			if (!bur.isEmpty()) {
				Collections.sort(bur, bu);
				t = bur.getFirst();
				totalTime += t.burstTime;
				turnaround = totalTime - t.arriveTime;
				waitingTime = turnaround - t.burstTime;
				result.add(new Result(t.id, t.burstTime, waitingTime));
				bur.removeFirst();
			} else
				totalTime++;
		}
		while (!bur.isEmpty()) {
			t = bur.getFirst();
			totalTime += t.burstTime;
			turnaround = totalTime - t.arriveTime;
			waitingTime = turnaround - t.burstTime;
			result.add(new Result(t.id, t.burstTime, waitingTime));
			bur.removeFirst();
		}

		prt(result, totalTime);
	}

	public static void HRRN(String fileName) throws IOException {
		System.out.println("HRRN");
		LinkedList<Process> arr = fileIO(fileName);
		LinkedList<Process> bur = new LinkedList<Process>();
		LinkedList<Result> result = new LinkedList<Result>();
		ArriveSort ar = new ArriveSort();
		PrioritySort pr = new PrioritySort();
		Process s, t;
		int totalTime = 0, turnaround, waitingTime;
		Collections.sort(arr, ar);

		while (!arr.isEmpty()) {
			turnaround = 0;
			waitingTime = 0;

			for (int i = 0; i < arr.size(); i++) {
				s = arr.get(i);
				if (totalTime >= s.arriveTime) {
					bur.add(new Process(s.id, s.arriveTime, s.burstTime, s.priority));
					arr.remove(i);
					i--;
				}
			}

			if (!bur.isEmpty()) {
				rePriority(bur, totalTime);
				Collections.sort(bur, pr.reversed());
				t = bur.getFirst();
				totalTime += t.burstTime;
				turnaround = totalTime - t.arriveTime;
				waitingTime = turnaround - t.burstTime;
				result.add(new Result(t.id, t.burstTime, waitingTime));
				bur.removeFirst();
			} else
				totalTime++;
		}
		while (!bur.isEmpty()) {
			rePriority(bur, totalTime);
			Collections.sort(bur, pr.reversed());
			t = bur.getFirst();
			totalTime += t.burstTime;
			turnaround = totalTime - t.arriveTime;
			waitingTime = turnaround - t.burstTime;
			result.add(new Result(t.id, t.burstTime, waitingTime));
			bur.removeFirst();
		}

		prt(result, totalTime);
	}

	public static void RR(String fileName) throws IOException {
		System.out.println("RR");
		LinkedList<Process> arr = fileIO(fileName);
		LinkedList<Process> bur = new LinkedList<Process>();
		LinkedList<Result> result = new LinkedList<Result>();
		ArriveSort ar = new ArriveSort();
		Process s, t;
		int totalTime = 0, turnaround, waitingTime, num;

		System.out.print("Time-slice 설정 : ");
		Scanner in = new Scanner(System.in);
		int timeSlice = in.nextInt();

		Collections.sort(arr, ar);
		while (!arr.isEmpty()) {
			turnaround = 0;
			waitingTime = 0;
			num = 0;
			for (int i = 0; i < arr.size(); i++) {
				s = arr.get(i);
				if (totalTime >= s.arriveTime) {
					num++;
					bur.add(new Process(s.id, s.arriveTime, s.burstTime, s.priority));
					arr.remove(i);
					i--;
				}
			}

			if (!bur.isEmpty()) {
				if (num != 0) {
					if (bur.size() - (num + 1) >= 0) {
						s = bur.get(bur.size() - 1 - num);
						bur.add(new Process(s.id, s.arriveTime, s.burstTime, s.priority, s.count));
						bur.remove(bur.size() - 2 - num);
					}
				}
				t = bur.getFirst();
				if (t.burstTime <= timeSlice) {
					totalTime += t.burstTime;
					turnaround = totalTime - t.arriveTime;
					t.burstTime = (t.burstTime + (t.count * timeSlice));
					waitingTime = turnaround - t.burstTime;
					result.add(new Result(t.id, t.burstTime, waitingTime));
				} else {
					totalTime += timeSlice;
					t.burstTime -= timeSlice;
					bur.add(new Process(t.id, t.arriveTime, t.burstTime, t.priority, (t.count + 1)));
				}
				bur.removeFirst();
			} else
				totalTime++;
		}
		while (!bur.isEmpty()) {
			t = bur.getFirst();
			if (t.burstTime <= timeSlice) {
				totalTime += t.burstTime;
				turnaround = totalTime - t.arriveTime;
				t.burstTime = (t.burstTime + (t.count * timeSlice));
				waitingTime = turnaround - t.burstTime;
				result.add(new Result(t.id, t.burstTime, waitingTime));
			} else {
				totalTime += timeSlice;
				t.burstTime -= timeSlice;
				bur.add(new Process(t.id, t.arriveTime, t.burstTime, t.priority, (t.count + 1)));
			}
			bur.removeFirst();
		}

		prt(result, totalTime);
	}

	public static void rePriority(LinkedList<Process> arr, int totalTime) {
		Process t;
		int waitingTime;
		for (int i = 0; i < arr.size(); i++) {
			t = arr.get(i);
			waitingTime = totalTime - t.arriveTime;
			t.priority = 1 + ((double) waitingTime / t.burstTime);
			arr.set(i, t);
		}
	}

	public static LinkedList<Process> fileIO(String fileName) throws IOException {
		String line;
		Process s;
		LinkedList<Process> arr = new LinkedList<Process>();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

		while ((line = bufferedReader.readLine()) != null) {
			String token[] = line.split(" ");
			s = new Process(Integer.parseInt(token[0]), Integer.parseInt(token[1]), Integer.parseInt(token[2]),
					Double.parseDouble(token[3]));
			arr.add(s);
		}

		bufferedReader.close();
		return arr;
	}

	public static void prt(LinkedList<Result> result, int totalTime) {
		int waitingTime = 0;
		System.out.println("---------------------------------");
		System.out.println("출력: ");
		for (int i = 0; i < result.size(); i++) {
			Result s = result.get(i);
			waitingTime += s.waitingTime;
			System.out.println(s);
		}
		System.out.println("전체 실행시간 : " + totalTime + "\n평균 대기시간 : "
				+ String.format("%.2f", ((double) waitingTime / result.size())));
		System.out.println("---------------------------------");
		System.out.println();
	}

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("파일 입력 : ");
		String fileName = in.next();
		FCFS(fileName);
		SJF(fileName);
		HRRN(fileName);
		RR(fileName);

		System.out.println("종료하려면 0을 입력하세요.");
		int a = in.nextInt();
		if (a == 0)
			return;
	}
}

class ArriveSort implements Comparator<Process> {
	public int compare(Process o1, Process o2) {
		int cmp;
		cmp = Integer.compare(o1.arriveTime, o2.arriveTime);
		return cmp;
	}
}

class BurstSort implements Comparator<Process> {
	public int compare(Process o1, Process o2) {
		int cmp;
		cmp = Integer.compare(o1.burstTime, o2.burstTime);
		if (cmp == 0) {
			cmp = Integer.compare(o1.arriveTime, o2.arriveTime);
		}
		return cmp;
	}
}

class PrioritySort implements Comparator<Process> {
	public int compare(Process o1, Process o2) {
		int cmp = Double.compare(o1.priority, o2.priority);
		return cmp;
	}
}

class Process {
	public int id;
	public int arriveTime;
	public double priority;
	public int burstTime;
	public int count;

	public Process() {
		this(0, 0, 0, 0, 0);
	}

	public Process(int id, int arriveTime, int burstTime) {
		this(id, arriveTime, burstTime, 0, 0);
	}

	public Process(int id, int arriveTime, int burstTime, double priority) {
		this(id, arriveTime, burstTime, priority, 0);
	}

	public Process(int id, int arriveTime, int burstTime, double priority, int count) {
		this.id = id;
		this.arriveTime = arriveTime;
		this.priority = priority;
		this.burstTime = burstTime;
		this.count = count;
	}
}

class Result {
	public int id;
	public int burstTime;
	public int waitingTime;

	public Result(int id, int burstTime, int waitingTime) {
		this.id = id;
		this.burstTime = burstTime;
		this.waitingTime = waitingTime;
	}

	public String toString() {
		return "(id :" + id + ", burstTime : " + burstTime + ", waitingTime : " + waitingTime + ")";
	}
}
