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
		//if the size of the gird is < 1 or
		//if the number of tests is < 1, throw an error
		if (N<1 || T<1) {
			throw new IllegalArgumentException();
		}
		
		//initializes new arraylist (containing coordinates of all sites in the grid) and list of p values
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
		
		//shuffles order of arraylist, creates new percolation object...
		for (int t = 0; t < T; t++) {
			Collections.shuffle(sites,ourRandom);
			IUnionFind finder = new QuickUWPC();
			IPercolate sim = new PercolationUF(N,finder);
			//IPercolate sim = new PercolationDFSFast(N);
			int j = 0;
			double psum = 0;
			//... and keeps openening sites until the grid percolates, at which point the ratio of open sites to total sites is recorded and stores in an array
			while (!sim.percolates()) {
				Integer[] temp = sites.get(j);
				int a = temp[0];
				int b = temp[1];
				sim.open(a,b);
				j++;
				pvals[t] = (double)sim.numberOfOpenSites()/(double)(N*N);		
			}
		}
		
		//generates all important statistical values using peripheral methods
		mean = mean(pvals);
		stddev = stddev(pvals);
		var = var(pvals);
		num = T;
		confidenceLow = confidenceLow();
		confidenceHigh = confidenceHigh();
		
		
	}
	
	
	public static void main(String[] args) {
		
		int size = 400;
		int simNum = 100;
//		double ttime = 0;
//		double mmean = 0;
//		for (int t = 0; t<5; t++) {
//			double start =  System.nanoTime();
//			PercolationStats Test = new PercolationStats(size,simNum);
//			double end =  System.nanoTime();
//			double time =  (end-start)/1e9;
//			mmean += Test.mean();
//			ttime += time;
//			System.out.printf("mean: %1.4f, time: %1.4f\n",Test.mean(),time);
//		}
//		System.out.printf("Overall mean: %1.4f, Overall time: %1.4f\n",mmean/5,ttime/5);
		
		double start =  System.nanoTime();
		PercolationStats ps = new PercolationStats(size,simNum);
		double end =  System.nanoTime();
		double time =  (end-start)/1e9;
		System.out.printf("mean: %1.4f, time: %1.4f\n",ps.mean(),time);


//		System.out.println("Mean: " + Test.mean);
//		System.out.println("Standard Deviation: " + Test.stddev);
//		System.out.println("Variance: " + Test.var);
//		System.out.println("Number of tests: " + Test.num);
//		System.out.println("Confidence High: " + Test.confidenceHigh());
//		System.out.println("Confidence Low: " + Test.confidenceLow());
//		System.out.println("Test C.L.: " + (Test.mean()-1.96*Test.stddev())/Math.sqrt(10));
		
	}
	
	
	public static double mean (double[] a) {
		//finds average of an array of doubles
        if (a.length == 0) return Double.NaN;
        double sum = sum(a);
        return sum / a.length;
    }
	
	public double mean() {
		//returns average of all p values
        return mean;
    }
	
	private static double sum(double[] a) {
		//returns sum of an array of doubles
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }
	
	public static double stddev(double[] a) {
		//returns the standard deviation of an array of doubles
        return Math.sqrt(var(a));
    }
	
	public double stddev() {
		//returns standard deviation of all p values
        return stddev;
    }
	
	
	public static double var(double[] a) {
		// returns variance of an array of doubles
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
	
	public double confidenceLow() {
		//lower bound of collection of 95% of values
		return this.mean-(1.96*this.stddev)/Math.sqrt(this.num);
		
	}
	
	public double confidenceHigh() {
		//upper bound of collection of 95% of values
		return this.mean+(1.96*this.stddev)/Math.sqrt(this.num);
		
	}
	
}
