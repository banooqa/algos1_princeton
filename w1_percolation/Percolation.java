// ****************************************************************************
// *  Name:
// *  Date:
// *  Description:
// ****************************************************************************

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] mainarr;
    private final int n;
    private WeightedQuickUnionUF wqu;

    private enum Nbr {
        L, R, T, B, VT, VB
    }


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        this.mainarr = new boolean[n * n + 2];
        for (int i = 0; i < mainarr.length - 2; i++)
            this.mainarr[i] = false; // false representing a closed or blocked site
        this.mainarr[n * n] = true;            // top virtual site
        this.mainarr[n * n + 1] = true;        // bottom virtual site
        wqu = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            mainarr[xytoz(row, col)] = true;
            // 1 representing an open site
            if (leftExists(col))
                handle(row, col, Nbr.L);
            if (rightExists(col))
                handle(row, col, Nbr.R);
            if (topExists(row))
                handle(row, col, Nbr.T);
            else
                handle(row, col, Nbr.VT);
            if (bottomExists(row))
                handle(row, col, Nbr.B);
            else
                handle(row, col, Nbr.VB);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return (mainarr[xytoz(row, col)]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return (wqu.connected(xytoz(row, col), n * n));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < mainarr.length - 2; i++) {
            if (mainarr[i])
                count++;
        }
        return count;
    }

    // finding neighbors
    private boolean leftExists(int col) {
        return (col > 1);
    }

    private boolean rightExists(int col) {
        return (col < n);
    }

    private boolean topExists(int row) {
        return (row > 1);
    }

    private boolean bottomExists(int row) {
        return (row < n);
    }

    // connecting neighbors
    private void handle(int row, int col, Nbr nbr) {
        switch (nbr) {
            case L:
                if (isOpen(row, col - 1))
                    wqu.union(xytoz(row, col), xytoz(row, col - 1));
                break;
            case R:
                if (isOpen(row, col + 1))
                    wqu.union(xytoz(row, col), xytoz(row, col + 1));
                break;
            case T:
                if (isOpen(row - 1, col))
                    wqu.union(xytoz(row, col), xytoz(row - 1, col));
                break;
            case B:
                if (isOpen(row + 1, col))
                    wqu.union(xytoz(row, col), xytoz(row + 1, col));
                break;
            case VT:
                wqu.union(xytoz(row, col), (n * n));
                break;
            case VB:
                wqu.union(xytoz(row, col), (n * n + 1));
                break;
        }
    }

    // does the system percolate?
    public boolean percolates() {
        // TODO handle backwash
        return (wqu.connected(n * n, n * n + 1));
    }

    // converting row and col to id
    private int xytoz(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("invalid input");
        }
        else return (row - 1) * n + (col - 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        // test client (optional)
    }
}
