package javaSort2023;

public class mergeTD extends Abstract {
	private static void merge(Comparable a[], Comparable aux[], int lo, int mid, int hi) {
		for (int i = 0; i < a.length; i++)
			aux[i] = a[i];

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				a[k] = aux[j++];
			else if (j > hi)
				a[k] = aux[i++];
			else if (less(aux[j], aux[i]))
				a[k] = aux[j++];
			else
				a[k] = aux[i++];
		}
	}

	public static void sort(Comparable[] a) {
		Comparable aux[] = new Comparable[a.length];
		sort(a, aux, 0, a.length - 1);
	}

	private static void sort(Comparable a[], Comparable aux[], int lo, int hi) {
		if (hi <= lo)
			return;
		int mid = lo + (hi - lo) / 2;
		sort(a, aux, lo, mid);
		sort(a, aux, mid + 1, hi);
		merge(a, aux, lo, mid, hi);
	}

	public static void main(String[] args) {
		Integer a[] = { 1, 3, 4, 6, 4, 8, 9, 10, 14, 11, 49, 20 };
		sort(a);
		Show(a);
	}
}
