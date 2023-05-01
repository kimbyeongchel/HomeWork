package javaSort2023;

public class Radix {
	public static void sort(int a[]) {
		int i, m = a[0], exp = 1, n = a.length;
		int b[] = new int [n];
		
		for(i= 1 ; i < n ; i++)
			if(m < a[i])
				m = a[i];
		
		while(m / exp > 0)
		{
			int c[] = new int [10];
			for(i = 0 ; i < n ; i++)
				c[a[i]/exp % 10]++;
			for(i = 1 ; i <n ; i++)
				c[i] += c[i-1];
			for(i = n-1 ; i >= 0 ; i--)
				b[--c[a[i]/exp % 10]] = a[i];
			for(i = 0 ; i < n ; i++)
				a[i] = b[i];
			exp *= 10;
		}
	}
}
