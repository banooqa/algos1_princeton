import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int n;
    private int[][] tilescopy;
    private List<Board> q;

    private enum Nbr {
        L, R, U, D
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        n = tiles.length;
        tilescopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tilescopy[i][j] = tiles[i][j];
            }
        }
    }

    private int abs(int a) {
        return (a >= 0) ? a : -1 * a;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(n * n);
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%d", tilescopy[i][j])).append("\t");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hd = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1 && tilescopy[i][j] != 0) {
                    hd++;
                    break;
                }
                if (tilescopy[i][j] != i * n + j + 1 && tilescopy[i][j] != 0) {
                    hd++;
                }
            }
        }
        return hd;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int md = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1 && tilescopy[i][j] == 0)
                    break;
                if (tilescopy[i][j] != i * n + j + 1 && tilescopy[i][j] != 0) {
                    int k = tilescopy[i][j];
                    int ki = (k - 1) / n;
                    int kj = (k - 1) % n;
                    md += abs(ki - i) + abs(kj - j);
                }
            }
        }
        return md;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (tilescopy[n - 1][n - 1] != 0)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    if (tilescopy[i][j] == 0) return true;
                }
                if (tilescopy[i][j] != i * n + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.tilescopy, that.tilescopy);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zi = 0;
        int zj = 0;
        q = new ArrayList<Board>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tilescopy[i][j] == 0) {
                    zi = i;
                    zj = j;
                }
            }
        }
        if (leftExists(zj)) {
            handle(zi, zj, Nbr.L);
        }
        if (rightExists(zj)) {
            handle(zi, zj, Nbr.R);
        }
        if (topExists(zi)) {
            handle(zi, zj, Nbr.U);
        }
        if (bottomExists(zi)) {
            handle(zi, zj, Nbr.D);
        }
        return q;
    }

    private boolean leftExists(int j) {
        return (j > 0);
    }

    private boolean rightExists(int j) {
        return (j < n - 1);
    }

    private boolean topExists(int i) {
        return (i > 0);
    }

    private boolean bottomExists(int i) {
        return (i < n - 1);
    }

    // connecting neighbors
    private void handle(int i, int j, Nbr nbr) {
        switch (nbr) {
            case L:
                Board neighbor = new Board(tilescopy);
                exch(neighbor, i, j, i, j - 1);
                q.add(neighbor);
                break;
            case R:
                Board neighbor1 = new Board(tilescopy);
                exch(neighbor1, i, j, i, j + 1);
                q.add(neighbor1);
                break;
            case U:
                Board neighbor2 = new Board(tilescopy);
                exch(neighbor2, i, j, i - 1, j);
                q.add(neighbor2);
                break;
            case D:
                Board neighbor3 = new Board(tilescopy);
                exch(neighbor3, i, j, i + 1, j);
                q.add(neighbor3);
                break;
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tilescopy);
        if (tilescopy[0][0] != 0 && tilescopy[0][1] != 0) {
            exch(twin, 0, 0, 0, 1);
        }
        else exch(twin, 1, 0, 1, 1);
        return twin;
    }

    private Board exch(Board a, int i, int j, int x, int y) {
        int temp = a.tilescopy[i][j];
        a.tilescopy[i][j] = a.tilescopy[x][y];
        a.tilescopy[x][y] = temp;
        return a;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial);
        StdOut.println("Hamming = " + initial.hamming());
        StdOut.println("Manhattan = " + initial.manhattan());
        Board twn2 = initial.twin();
        Board twin = new Board(tiles);
        StdOut.println(twn2);
        StdOut.println(twn2.equals(twin));
        StdOut.println(initial.equals(twin));


    }

}
