public class MatrixPath {
	public static int[][] path(int[][] m) {
		int[][] c = new int[m.length][m[0].length];
		int select = 0;

		c[0][0] = m[0][0];

		for (int i = 1; i < m.length; i++) {
			c[i][0] = m[i][0] + c[i - 1][0];
		}

		for (int j = 1; j < m[0].length; j++) {
			c[0][j] = m[0][j] + c[0][j - 1];
		}

		for (int i = 1; i < m.length; i++) {
			for (int j = 1; j < m[i].length; j++) {
				select = Math.max(c[i][j - 1], c[i - 1][j]);
				c[i][j] = m[i][j] + select;
			}
		}

		return c;
	}

	public static void main(String[] args) {
		int[][] m = { { 6, 7, 12, 5 }, { 5, 3, 11, 18 }, { 7, 17, 3, 3 }, { 8, 10, 14, 9 } };

		int[][] c = path(m);

		System.out.println("최대 경로의 합: " + c[m.length - 1][m[0].length - 1]);
	}
}