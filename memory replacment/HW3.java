import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class HW3 {
	private static final int ACCESS_TIME = 8;

	public static void FIFO(String refString, int frame) {
		Queue<String> page = new LinkedList<String>();
		int hit = 0, page_fault = 0;
		long eat = 0;
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < refString.length(); i++) {
			String chr = refString.substring(i, i + 1);

			if (!page.contains(chr)) {
				page_fault++;
				if (page.size() >= frame)
					page.remove();
				page.add(chr);
				eat += ACCESS_TIME;
			} else {
				hit++;
			}
		}
		long endTime = System.currentTimeMillis();

		prt(page_fault, hit, refString, "FIFO", eat, (endTime - startTime));
	}

	public static void Optimal(String refString, int frame) {
		HashMap<Character, Integer> pageTable = new HashMap<>();
		int page_fault = 0, hit = 0;
		long eat = 0;
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < refString.length(); i++) {
			char page = refString.charAt(i);

			if (pageTable.containsKey(page)) {
				hit++;
			} else {
				if (pageTable.size() >= frame) {
					char pageToRemove = findOptimalRemovePage(refString, pageTable, i);
					pageTable.remove(pageToRemove);
				}
				pageTable.put(page, i);
				page_fault++;
				eat += ACCESS_TIME;
			}
		}

		long endTime = System.currentTimeMillis();
		prt(page_fault, hit, refString, "Optimal", eat, (endTime - startTime));
	}

	private static char findOptimalRemovePage(String refString, HashMap<Character, Integer> pageTable,
			int currentIndex) {
		char pageToRemove = ' ';
		int farIndex = -1;

		for (char page : pageTable.keySet()) {
			int index = refString.indexOf(page, currentIndex + 1);
			if (index == -1) {
				return page;
			}
			if (index > farIndex) {
				farIndex = index;
				pageToRemove = page;
			}
		}
		return pageToRemove;
	}

	public static void LRU(String refString, int frame) {
		HashMap<Character, Integer> pageTable = new HashMap<>();
		int page_fault = 0, hit = 0;
		long eat = 0;
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < refString.length(); i++) {
			char page = refString.charAt(i);

			if (pageTable.containsKey(page)) {
				hit++;
			} else {
				if (pageTable.size() >= frame) {
					char pageToRemove = findRemovePage(refString, pageTable, i);
					pageTable.remove(pageToRemove);
				}
				pageTable.put(page, i);
				page_fault++;
				eat += ACCESS_TIME;
			}
		}
		long endTime = System.currentTimeMillis();
		prt(page_fault, hit, refString, "LRU", eat, (endTime - startTime));
	}

	private static char findRemovePage(String refString, HashMap<Character, Integer> pageTable, int currentIndex) {
		char pageToRemove = ' ';
		int farIndex = refString.length();

		for (Map.Entry<Character, Integer> entry : pageTable.entrySet()) {
			char page = entry.getKey();
			int index = refString.lastIndexOf(page, currentIndex - 1);
			if (index < farIndex) {
				farIndex = index;
				pageToRemove = page;
			}
		}

		return pageToRemove;
	}

	public static void LFU(String refString, int frame) {
		HashMap<Character, Integer> pageTable = new HashMap<>();
		int page_fault = 0, hit = 0;
		long eat = 0;
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < refString.length(); i++) {
			char page = refString.charAt(i);

			if (pageTable.containsKey(page)) {
				hit++;
			} else {
				if (pageTable.size() >= frame) {
					char pageToRemove = findLFURemovePage(refString, pageTable, i);
					pageTable.remove(pageToRemove);
				}
				pageTable.put(page, i);
				page_fault++;
				eat += ACCESS_TIME;
			}
		}
		long endTime = System.currentTimeMillis();
		prt(page_fault, hit, refString, "LFU", eat, (endTime - startTime));
	}

	private static char findLFURemovePage(String refString, HashMap<Character, Integer> pageTable, int currentIndex) {
		char pageToRemove = ' ';
		int smallFre = refString.length();
		HashMap<Character, Integer> frequency = new HashMap<>();
		List<Character> pagesToRemove = new ArrayList<>();

		for (char page : pageTable.keySet()) {
			int count = 0;
			for (int i = 0; i < currentIndex; i++) {
				if (page == refString.charAt(i)) {
					count++;
				}
			}

			frequency.put(page, count);

			if (smallFre > count) {
				smallFre = count;
				pageToRemove = page;
			}
		}

		for (Map.Entry<Character, Integer> small : frequency.entrySet()) {
			if (small.getValue() != smallFre)
				pagesToRemove.add(small.getKey());
		}

		for (char page : pagesToRemove) {
			frequency.remove(page);
		}

		if (frequency.size() != 1) {
			pageToRemove = findRemovePage(refString, frequency, currentIndex);
		}

		return pageToRemove;
	}

	public static void prt(int page_fault, int hit, String refString, String algorithm, long eat, long totalTime) {
		System.out.println(algorithm);
		System.out.println("-------------------------");
		System.out.println("falut: " + page_fault + " hit: " + hit);
		System.out.println("Page Fault Rate : " + Math.round((double) (page_fault * 100) / refString.length()) + "%");
		System.out.println("성능 지연 시간: " + eat + "ms");
		System.out.println("전체 실행 시간: " + (totalTime + eat) + "ms");
		System.out.println("-------------------------");
		System.out.println();
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("disk AccessTime = 10ms로 가정");
		System.out.print("참조열 입력: ");
		String refString = in.next();
		System.out.print("프레임 입력: ");
		int frame = in.nextInt();

		FIFO(refString, frame);
		Optimal(refString, frame);
		LRU(refString, frame);
		LFU(refString, frame);

		System.out.println("종료하려면 0을 입력하세요.");
		int a = in.nextInt();
		if (a == 0)
			return;
	}
}
