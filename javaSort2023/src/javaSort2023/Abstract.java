package javaSort2023;

public abstract class Abstract {
	public static void sort(Comparable a[]) {};
	//less, exch, isSorted
	
	protected static boolean less(Comparable a, Comparable b)
	{
		return a.compareTo(b) < 0;
	}
	
	protected static void exch(Comparable a[], int i , int j)
	{
		Comparable b = a[i];
		a[i] = a[j];
		a[j] = b;
	}
	
	protected static void Show(Comparable a[])
	{
		for(int i = 0 ; i < a.length ; i++)
		{
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}
	
	protected static boolean isSorted(Comparable a[])
	{
		for(int i = 1 ; i < a.length ; i++)
		{
			if(less(a[i], a[i-1]))
				return false;
		}
		return true;
	}
}
