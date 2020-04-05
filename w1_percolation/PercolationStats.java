/* ************************************************************************ */
// Name: Banooqa Banday
// Date:
// Description:
// ************************************************************************ */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private int trials;
    private double[] thresh;
    private double mean, stddev;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (trials <= 0 || n <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;
        this.thresh = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            thresh[i] = 1.0 * perc.numberOfOpenSites() / (n * n);
        }
        this.mean = StdStats.mean(thresh);
        this.stddev = StdStats.stddev(thresh);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - ((1.96 * stddev) / java.lang.Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((1.96 * stddev) / java.lang.Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("Mean\t\t\t\t\t = " + ps.mean());
        System.out.println("Standard Deviation\t\t = " + ps.stddev());
        System.out.println("95% confidence interval\t = " + ps.confidenceLo() +
                                   ", " + ps.confidenceHi());

    }
}
