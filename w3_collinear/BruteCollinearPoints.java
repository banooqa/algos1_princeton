import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private int count;
    private final List<LineSegment> segs;

    public BruteCollinearPoints(Point[] points) {
        int n;
        count = 0;
        segs = new ArrayList<LineSegment>();
        if (points == null)
            throw new IllegalArgumentException();
        n = points.length;
        for (int i = 0; i < n; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                if (points[i].equals(points[j]))
                    throw new IllegalArgumentException();
            }
        }
        Point a, b, c, d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int m = k + 1; m < n; m++) {
                        a = points[i];
                        b = points[j];
                        c = points[k];
                        d = points[m];
                        if ((a.slopeTo(b) == a.slopeTo(c)) && (a.slopeTo(c) == a.slopeTo(d))) {
                            Point[] temp = { a, b, c, d };
                            Arrays.sort(temp);
                            segs.add(new LineSegment(temp[0], temp[3]));
                            count++;
                        }
                    }
                }
            }
        }
    }    // finds all line segments containing 4 points

    public int numberOfSegments() {
        return count;
    }       // the number of line segments

    public LineSegment[] segments() {
        return segs.toArray(new LineSegment[segs.size()]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
