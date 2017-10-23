import java.util.*;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {
	public static int RANDOM_SEED = 5678;
	public static Random ourRandom = new Random(RANDOM_SEED);
	public double mean = 0;
	public double stddev = 0;
	public int num = 0;
	public double var = 0;
	public double pvals[];
	public double confidenceLow;
	public double confidenceHigh;
	
	public PercolationStats(int N, int T) {
		if (N<1 || T<1) {
			throw new IllegalArgumentException();
		}
		ArrayList<Integer[]> sites = new ArrayList<Integer[]>();
		pvals = new double[T]; 
		for (int i = 0; i< N; i++) {
			for (int k = 0; k< N; k++) {
				Integer[] point = new Integer[2];
				point[0] = i;
				point[1] = k;
				sites.add(point);
			}
		}
		
		for (int t = 0; t < T; t++) {
			Collections.shuffle(sites,ourRandom);
			IUnionFind finder = new QuickFind();
			IPercolate sim = new PercolationUF(N,finder);
			int j = 0;
			double psum = 0;
			while (!sim.percolates()) {
				Integer[] temp = sites.get(j);
				int a = temp[0];
				int b = temp[1];
				sim.open(a,b);
				j++;
				pvals[t] = (double)sim.numberOfOpenSites()/(double)(N*N);		
			}
		}
		mean = mean(pvals);
		stddev = stddev(pvals);
		var = var(pvals);
		num = T;
		confidenceLow = confidenceLow();
		confidenceHigh = confidenceHigh();
		
		
	}
	
	
	public static void main(String[] args) {
		PercolationStats Test = new PercolationStats(20,10);
		System.out.println("Mean: " + Test.mean);
		System.out.println("Standard Deviation: " + Test.stddev);
		System.out.println("Variance: " + Test.var);
		System.out.println("Number of tests: " + Test.num);
		System.out.println("Confidence High: " + Test.confidenceHigh());
		System.out.println("Confidence Low: " + Test.confidenceLow());
		System.out.println("Test C.L.: " + (Test.mean()-1.96*Test.stddev())/Math.sqrt(10));
		
	}
	
	
	public static double mean (double[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = sum(a);
        return sum / a.length;
    }
	
	public double mean() {
        return mean;
    }
	
	private static double sum(double[] a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }
	
	public static double stddev(double[] a) {
        return Math.sqrt(var(a));
    }
	
	public double stddev() {
        return stddev;
    }
	
	
	public static double var(double[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
	
	public double confidenceLow() {
		
		return (this.mean-1.96*this.stddev)/Math.sqrt(this.num);
		
	}
	
	public double confidenceHigh() {
		
		return (this.mean+1.96*this.stddev)/Math.sqrt(this.num);
		
	}
	
}
