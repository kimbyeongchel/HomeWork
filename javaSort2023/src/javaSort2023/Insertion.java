package javaSort2023;

public class Insertion extends Abstract {
	public static void sort(Comparable a[]) {
		int N = a.length;
//		for (int i = 1; i < N; i++)
//			for (int j = i; j > 0 && less(a[j], a[j - 1]); j--)
//				exch(a, j, j - 1);
//
//		assert isSorted(a);
		int x, y;
		Comparable next;
		for (x = 1; x < N; x++) {
			next = a[x];
			for (y = x - 1; y >= 0 && (next.compareTo(a[y]) < 0); y--) {
				a[y+1] = a[y];
			}
			a[y+1] = next;
		}
	}

	public static void main(String args[]) {
		Integer a[] = { 10, 4, 5, 2, 1, 8, 3, 6 };
		sort(a);
		Show(a);
	}
}
