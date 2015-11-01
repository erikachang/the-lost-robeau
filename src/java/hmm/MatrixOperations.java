package hmm;

public class MatrixOperations {
	public static double[][] transpose(double m[][]) {
		int rows = m.length;
		int cols = m[0].length;
		
		double[][] n = new double[cols][rows];
		
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				n[i][j] = m[j][i];
			}
		}
		
		return n;
	}
	
	public static double[] pointwise(double vA[], double vB[]) {
		int lA = vA.length;
		int lB = vB.length;
		
		if (lA != lB) throw new UnsupportedOperationException("Vector sizes differ.");
		
		double [] vC = new double[lA];
		
		for (int i = 0; i < lA; i++) {
			vC[i] = vA[i] * vB[i];
		}
		
		return vC;
	}
	
	public static double[][] product(double mA[][], double mB[][]) {
		int rowsA = mA.length;
		int colsA = mA[0].length;
		int rowsB = mB.length;
		int colsB = mB[0].length;
		
		if (colsA != rowsB) throw new UnsupportedOperationException("colsA != rowsB");
		
		double[][] mC = new double[rowsA][colsB];
		
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				for (int k = 0; k < rowsB; k++) {
					mC[i][j] += mA[i][k] * mB[k][j];
				}
			}
		}
		
		return mC;
	}
	
	public static double[] product(double mA[][], double v[]) {
		int rowsA = mA.length;
		int colsA = mA[0].length;
		int lv = v.length;
		
		if (colsA != lv) throw new UnsupportedOperationException("colsA != lv");
		
		double[] x = new double[rowsA];
		
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < lv; j++) {
				x[i] += mA[i][j] * v[j];
			}
		}
		
		return x;
	}
	
	public static double[] product(double v[], double mB[][]) {
		int lv = v.length;
		int rowsB = mB.length;
		int colsB = mB[0].length;
		
		if (lv != rowsB) throw new UnsupportedOperationException("lv != rowsB");
		
		double[] x = new double[colsB];
		
		for (int i = 0; i < colsB; i++) {
			for (int j = 0; j < lv; j++) {
				x[i] += mB[j][i] * v[j];
			}
		}
		
		return x;
	}

}
