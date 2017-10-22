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
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	public double mean = 0;
	public double stddev = 0;
	public int num = 0;
	public double var = 0;
	public double pvals[];
	
	public PercolationStats(int N, int T) {
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
			System.out.println("ourRandom: " + ourRandom);
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
		for (int z = 0; z < pvals.length; z++) {
			System.out.println(pvals[z]);
		}
		mean = mean(pvals);
		stddev = stddev(pvals);
		var = var(pvals);
		num = T;
		
	}
	
	
	public static void main(String[] args) {
		PercolationStats Test = new PercolationStats(50,6);
		System.out.println("Mean: " + Test.mean);
		System.out.println("Standard Deviation: " + Test.stddev);
		System.out.println("Variance: " + Test.var);
		System.out.println("Number of tests: " + Test.num);
		System.out.println("Confidence High: " + Test.confidenceHigh());
		System.out.println("Confidence Low: " + Test.confidenceLow());
		
		
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
		
		return (mean-1.96*stddev)/Math.sqrt(num);
		
	}
	
	public double confidenceHigh() {
		
		return (mean+1.96*stddev)/Math.sqrt(num);
		
	}
	
}
