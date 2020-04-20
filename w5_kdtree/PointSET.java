import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;
    private Point2D nearest;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (points == null)
            throw new IllegalArgumentException();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> pinRect = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                pinRect.add(p);
        }
        return pinRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        int flag = 0;
        double dist = 0;
        for (Point2D point : points) {
            if (p == point)
                continue;
            if (flag == 0) {
                nearest = point;
                dist = p.distanceSquaredTo(point);
                flag = 1;
            }
            if (p.distanceSquaredTo(point) < dist) {
                dist = p.distanceSquaredTo(point);
                nearest = point;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET a = new PointSET();
        a.insert(new Point2D(0.5, 0.5));
        a.insert(new Point2D(0.4, 0.3));
        a.insert(new Point2D(0.8, 0.3));
        a.insert(new Point2D(0.4, 0.8));
        a.insert(new Point2D(0.2, 0.9));
        a.draw();
        System.out.println(a.nearest(new Point2D(0.5, 0.5)));
        RectHV rct = new RectHV(0.25, 0.25, 0.8, 0.8);
        System.out.println(a.range(rct));
        rct.draw();
    }
}
