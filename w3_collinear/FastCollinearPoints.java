import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private int count;
    private List<LineSegment> ls;

    public FastCollinearPoints(Point[] points) {
        ls = new ArrayList<LineSegment>();
        List<Point> cpts = new ArrayList<Point>();
        if (points == null)
            throw new IllegalArgumentException();
        int n = points.length;
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }
        Point[] q = Arrays.copyOfRange(points, 0, n);
        Arrays.sort(q);
        for (int i = 0; i < n - 1; i++) {
            if (q[i].compareTo(q[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        q = new Point[n - 1];
        for (int i = 0; i < n; i++) {
            int j = 0;
            Point rf = points[i];
            for (int ii = 0; ii < n; ii++) {
                if (ii < i)
                    q[ii] = points[ii];
                else if (ii > i)
                    q[ii - 1] = points[ii];
            }
            Arrays.sort(q, rf.slopeOrder());
            while (j < n - 2) {
                cpts.add(rf);
                while (rf.slopeTo(q[j]) == rf.slopeTo(q[j + 1])) {
                    cpts.add(q[j]);
                    j++;
                    if (j == n - 2) {
                        cpts.add(q[j]);
                        if (cpts.size() >= 4)
                            addLineSegment(cpts);
                        break;
                    }
                }
                if (j < n - 2) {
                    cpts.add(q[j]);
                    if (cpts.size() >= 4)
                        addLineSegment(cpts);
                }
                cpts.clear();
                j++;
            }
        }
    }    // finds all line segments containing 4 points

    private void addLineSegment(List<Point> cpts) {
        Point ref = cpts.get(0);
        Point small = cpts.get(0);
        Point large = cpts.get(0);
        for (Point pt : cpts) {
            if (pt.compareTo(small) < 0) small = pt;
            else if (pt.compareTo(large) > 0) large = pt;
        }
        LineSegment nls = new LineSegment(small, large);
        if (small == ref) {
            ls.add(nls);
            count++;
        }
    }

    public int numberOfSegments() {
        return count;
    }       // the number of line segments

    public LineSegment[] segments() {
        return ls.toArray(new LineSegment[ls.size()]);
    } // the line segments

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
