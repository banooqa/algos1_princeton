import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Node goal;

    private static class Node {
        private int moves;
        private Board board;
        private Node prev;
        private int man;

        public Node(Board initial) {
            moves = 0;
            prev = null;
            board = initial;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        POrder order = new POrder();
        MinPQ<Node> PQ = new MinPQ<Node>(order);
        MinPQ<Node> twinPQ = new MinPQ<Node>(order);
        Node node = new Node(initial);
        Board twin = initial.twin();
        Node tnode = new Node(twin);
        PQ.insert(node);
        twinPQ.insert(tnode);


        Node min = PQ.delMin();
        Node tmin = twinPQ.delMin();

        while (!(min.board.isGoal() || tmin.board.isGoal())) {
            for (Board b : min.board.neighbors()) {
                if (min.prev == null || !b.equals(min.prev.board)) {
                    Node n = new Node(b);
                    n.moves = min.moves + 1;
                    n.prev = min;
                    n.man = n.board.manhattan();
                    PQ.insert(n);
                }
            }
            min = PQ.delMin();
            for (Board b : tmin.board.neighbors()) {
                if (tmin.prev == null || !b.equals(tmin.prev.board)) {
                    Node tn = new Node(b);
                    tn.moves = tmin.moves + 1;
                    tn.prev = tmin;
                    twinPQ.insert(tn);
                }
            }
            tmin = twinPQ.delMin();
        }

        if (min.board.isGoal()) {
            goal = min;
        }
        else goal = null;
    }

    private class POrder implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            int x = a.man + a.moves;
            int y = b.man + b.moves;
            return Integer.compare(x, y);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        else return goal.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> sol = new Stack<Board>();
        for (Node n = goal; n != null; n = n.prev) {
            sol.push(n.board);
        }
        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}
