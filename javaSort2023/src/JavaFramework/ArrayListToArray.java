package JavaFramework;

import java.util.ArrayList;

class ArrayListToArray {
	public static void main(String args[]) {
		ArrayList<Integer> al = new ArrayList<Integer>(); // Integer array list를 생성s
		al.add(1);
		al.add(2);
		al.add(3);
		al.add(4);
		System.out.println("Contents of al: " + al);
		Integer ia[] = new Integer[al.size()]; // 배열 준비.
		ia = al.toArray(ia); // toArray() 함수를 이용하여 array list의 내용을 배열에 저장
		int sum = 0;
		for (int i : ia) // Sum the array.
			sum += i;
		System.out.println("Sum is: " + sum);
	}
}