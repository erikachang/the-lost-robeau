package hmm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HiddenMarkovModel {
	private double[] prior;
	private double[][] transitionMatrix;

	private ArrayList<double[]> fVector, sVector;
	private ArrayList<double[][]> evidences;
	
	private int time;

	private HiddenMarkovModel(double[] p, double[][] t) {
		this.prior = p;
		this.transitionMatrix = t;
		this.time = 0;
		fVector = new ArrayList<double[]>();
		fVector.add(p);

		sVector = new ArrayList<double[]>();
		sVector.add(p);
		evidences = new ArrayList<double[][]>();
		evidences.add(null);
	}

	private static HiddenMarkovModel instance;

	public static void create(double[] p, double[][] t) {
		if (instance == null) {
			instance = new HiddenMarkovModel(p, t);
		}
	}

	public static HiddenMarkovModel get() {
		return instance;
	}

	public double[] getPrior() {
		return prior;
	}

	public double[][] getTransitionMatrix() {
		return transitionMatrix;
	}

	public int getTime() {
		return time;
	}

	private double[] normalise(double[] v) {
		double ratio = 0.0;
		double[] n = new double[v.length];

		for (double d : v) {
			ratio += d;
		}

		if (ratio > 0) {
			for (int i = 0; i < v.length; i++) {
				n[i] = v[i] / ratio;
			}
		}
		
		return n;
	}

	synchronized public void doFiltering(double[][] observation) {
		
		
		evidences.add(observation);
		
		double[][] ft = MatrixOperations.product(observation, transitionMatrix);
		double[] prior = fVector.get(time);
		double[] f = MatrixOperations.product(ft, prior);
		f = normalise(f);
		fVector.add(f);
		
		time++;
	}
	
	synchronized public void doSmoothing() {
		sVector = new ArrayList<double[]>(time);
		double[] bMessage = new double[prior.length];
		
		for (int i = 0; i < bMessage.length; i++) {
			bMessage[i] = 1.0;
		}
		
		for (int i = time; i > 0; i--) {
			double[] smooth = normalise(MatrixOperations.pointwise(fVector.get(i), bMessage));
			sVector.add(smooth);
			
			double[][] bk = MatrixOperations.product(transitionMatrix, evidences.get(i));
			bMessage = normalise(MatrixOperations.product(bk, bMessage));
		}
	}
	
	synchronized public List<Integer> getSmoothedPath() {
		List<Integer> path = new LinkedList<Integer>();
		
		for (int i = (sVector.size()-1); i >= 0; i--) {
			path.add(argmax(sVector.get(i)));
		}
		
		return path;
	}
	
	public double max(double[] list) {
		double max = 0.0;
		for (double d:list) {
			if (d > max)
				max = d;
		}
		return max;
	}
	
	public int argmax(double[] list) {
		double max = 0.0;
		int maxindex = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i] > max) {
				max = list[i];
				maxindex = i;
			}
		}
		return maxindex;
	}

	public double[] getProbabilities() {
		return fVector.get(time-1);
	}
}
