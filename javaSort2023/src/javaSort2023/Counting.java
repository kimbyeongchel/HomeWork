package javaSort2023;

public class Counting {
	public static int[] sort(int a[], int k) {
		int N = a.length;
		int i;
		int c[] = new int[k];
		int b[] = new int[N];

		for (i = 0; i < N; i++)
			c[a[i]]++;
		for (i = 1; i < k; i++)
			c[i] += c[i - 1];
		for (i = N - 1; i >= 0; i--)
			b[--c[a[i]]] = a[i];
		return b;
	}

	public static void main(String args[]) {
		int a[] = { 10, 4, 5, 8, 1, 8, 3, 6 };
		int B[];
		B = Counting.sort(a, 11);
		for (int i = 0; i < B.length; i++)
			System.out.print(B[i] + " ");
		System.out.println();

	}
}
