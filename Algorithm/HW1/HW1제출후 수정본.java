import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class HW1 {
	public static int search(ArrayList user, Contents key) {
		int lo = 0, hi = user.size() - 1;
		AlphaSort al = new AlphaSort();
		int mid;
		Contents s1;

		while (lo <= hi) {
			mid = (lo + hi) / 2;
			s1 = (Contents) user.get(mid);
			int cmp = al.compare(s1, key);
			if (cmp == 0)
				return mid;
			else if (cmp > 0) {
				hi = mid - 1;
			} else
				lo = mid + 1;
		}
		return 0;
	}

	public static List[] fileIO(String fileName) throws IOException {
		String line;
		int num, flag;
		Contents s1, s2;
		AlphaSort al = new AlphaSort();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

		num = Integer.parseInt(bufferedReader.readLine());
		List user[] = new List[num];

		for (int i = 0; i < num; i++)
			user[i] = new LinkedList<Contents>();

		while ((line = bufferedReader.readLine()) != null) {
			String token[] = line.split(" ");
			s1 = new Contents(token[1], Double.parseDouble(token[2]));
			flag = 0;

			for (int j = 0; j < user[Integer.parseInt(token[0])].size(); j++) {
				s2 = (Contents) user[Integer.parseInt(token[0])].get(j);
				if (s2.content.equals(s1.content)) {
					user[Integer.parseInt(token[0])].set(j, s1);
					flag = 1;
					break;
				}

			}
			if (flag == 0)
				user[Integer.parseInt(token[0])].add(new Contents(token[1], Double.parseDouble(token[2])));
		}
		bufferedReader.close();

		for (int i = 0; i < num; i++)
			Collections.sort(user[i], al);

		return user;
	}

	public static void normalization(List[] user) {
		ListIterator<Contents> litr;
		Contents ss;
		for (int i = 0; i < user.length; i++) {
			if (user[i].size() == 0)
				continue;
			double average = 0;
			litr = user[i].listIterator();
			while (litr.hasNext()) {
				ss = litr.next();
				average += ss.score;
			}
			average /= user[i].size();

			while (litr.hasPrevious()) {
				ss = litr.previous();
				ss.score -= average;
				litr.set(ss);
			}
		}
	}

	public static ArrayList<Contents> similarity(List[] user, int target) {
		AlphaSort al = new AlphaSort();
		ScoreSort sc = new ScoreSort();
		Contents s1, s2;
		ArrayList<Contents> arr = new ArrayList<Contents>();

		double sumTarget, sumOther, value;
		int i = 0, cmp, remem;

		while (i < user.length) {
			if (target == i) {
				i++;
				continue;
			}

			value = 0;
			sumTarget = 0;
			sumOther = 0;
			remem = 0;

			for (int k = 0; k < user[i].size(); k++) {
				s2 = (Contents) user[i].get(k);
				sumOther += (s2.score * s2.score);
			}

			for (int j = 0; j < user[target].size(); j++) {
				s1 = (Contents) user[target].get(j);
				sumTarget += (s1.score * s1.score);
				for (int k = remem; k < user[i].size(); k++) {
					s2 = (Contents) user[i].get(k);
					cmp = al.compare(s1, s2);
					if (cmp == 0) {
						value += (s1.score * s2.score);
						remem = k + 1;
						break;
					}
				}
			}
			i++;
			value /= (Math.sqrt(sumOther) * Math.sqrt(sumTarget));
			if (Double.isNaN(value))
				value = 0;

			arr.add(new Contents((Integer.toString(i - 1)), value));
		}
		Collections.sort(arr, sc.reversed());
		return arr;
	}

	public static ArrayList<Contents> recommend(List[] user, ArrayList<Contents> arr, int target, int n) {
		Contents s1, s2;
		AlphaSort al = new AlphaSort();
		ScoreSort sc = new ScoreSort();
		ArrayList<Contents> prop = new ArrayList<Contents>();
		int id, cmp;

		for (int i = 0; i < arr.size() && i < n; i++) {
			id = Integer.parseInt(arr.get(i).content);
			for (int j = 0; j < user[id].size(); j++) {
				s1 = (Contents) user[id].get(j);
				s1.score *= arr.get(i).score;
				cmp = search(prop, s1);
				if (cmp != 0) {
					s2 = prop.get(cmp);
					s1.score += s2.score;
					prop.set(cmp, s1);
				} else {
					prop.add(s1);
				}
				Collections.sort(prop, al);
			}
		}

		for (int j = 0; j < user[target].size(); j++) {
			s1 = (Contents) user[target].get(j);
			for (int i = 0; i < prop.size(); i++) {
				s2 = (Contents) prop.get(i);
				cmp = al.compare(s1, s2);
				if (cmp == 0) {
					prop.remove(i);
					break;
				}
			}
		}
		
		Collections.sort(prop, sc.reversed());
		return prop;
	}

	public static void prtCom(List user, int length) {
		Contents s;
		System.out.print("[");
		for (int j = 0; j < length && j < user.size(); j++) {
			s = (Contents) user.get(j);
			System.out.print(s);
			if (j != (length - 1) && (j != (user.size() - 1))) {
				System.out.print(", ");
			}
		}
		System.out.println("]");
		System.out.println();
	}

	public static void prtNormal(List[] user, int target) {
		System.out.print("1. 사용자 " + target + "의 콘텐츠와 정규화 점수:\n   ");
		prtCom(user[target], user[target].size());
	}

	public static void prtSimilar(ArrayList<Contents> arr, int n) {
		System.out.println("2. 유사한 사용자 id와 유사도 리스트");
		for (int j = 0; j < arr.size() && j < n; j++) {
			System.out.printf("   사용자 id: %s, 유사도 : %.6f\n", arr.get(j).content, arr.get(j).score);
		}
		System.out.println();
	}

	public static void prtRec(ArrayList<Contents> arr, int target, int k) {
		System.out.print("3. 사용자 " + target + "에게 추천할 콘텐츠와 추천 점수\n   ");
		prtCom(arr, k);
	}

	public static void main(String[] args) throws IOException {
		
		System.out.print("파일 이름, target 사용자, 참고인 수, 항목 수? ");
		List[] user;
		Scanner in = new Scanner(System.in);
		String fileName = in.next();
		int target = in.nextInt();
		int n = in.nextInt();
		int k = in.nextInt();

		in.close();
		
		long start = System.currentTimeMillis();

		user = fileIO(fileName);
		normalization(user);
		prtNormal(user, target);
		ArrayList<Contents> arr = similarity(user, target);
		prtSimilar(arr, n);
		ArrayList<Contents> rec = recommend(user, arr, target, n);
		prtRec(rec, target, k);
		
		long end = System.currentTimeMillis();
		System.out.println((end - start)/1000.0);
	}
}

class AlphaSort implements Comparator<Contents> {
	public int compare(Contents a, Contents b) {
		String eng1 = a.content.substring(0, 1);
		String eng2 = b.content.substring(0, 1);

		int cmp = eng1.compareTo(eng2);
		if (cmp == 0) {
			int num1 = Integer.parseInt(a.content.substring(1));
			int num2 = Integer.parseInt(b.content.substring(1));

			cmp = Integer.compare(num1, num2);
		}
		return cmp;
	}
}

class ScoreSort implements Comparator<Contents> {
	public int compare(Contents a, Contents b) {
		return Double.compare(a.score, b.score);
	}
}

class Contents {
	String content;
	double score;

	Contents(String content, double score) {
		this.content = content;
		this.score = score;
	}

	public String toString() {
		return "(" + this.content + ", " + String.format("%.3f", this.score) + ")";
	}
}