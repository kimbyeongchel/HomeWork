public class Floyd {
	public static void floyd(int n, int[][] W, int[][] D, int p[][]) {
		int i, j, k;

		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				D[i][j] = W[i][j];
			}
		}

		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				p[i][j] = -1;
			}
		}

		for (k = 0; k < n; k++) {
			for (i = 0; i < n; i++) {
				for (j = 0; j < n; j++) {
					if (D[i][k] + D[k][j] < D[i][j]) {
						p[i][j] = k;
						D[i][j] = D[i][k] + D[k][j];
					}
				}
			}
		}

		path(5 - 1, 3 - 1, p);
	}

	public static void path(int q, int r, int[][] p) {
		if (p[q][r] != -1) {
			path(q, p[q][r], p);
			System.out.println(" v" + (p[q][r] + 1));
			path(p[q][r], r, p);
		}
	}

	public static void main(String[] args) {
		int n = 5;
		int[][] W = { { 0, 1, 800, 1, 5 }, { 9, 0, 3, 2, 800 }, { 800, 800, 0, 4, 800 }, { 800, 800, 2, 0, 3 },
				{ 3, 800, 800, 800, 0 } };

		int[][] p = new int[n][n];
		int[][] D = new int[n][n];
		floyd(n, W, D, p);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(D[i][j] + " ");
			}
			System.out.println();
		}
	}
}