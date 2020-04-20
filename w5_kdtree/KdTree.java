import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private final SET<Point2D> points;
    private List<Point2D> rectPoints;
    private Point2D nearest;
    private double distSqr;

    // construct an empty set of points
    public KdTree() {
        points = new SET<Point2D>();
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lst;        // the left/bottom subtree
        private Node rst;        // the right/top subtree

        public Node(double x, double y) {
            this.p = new Point2D(x, y);
        }
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
        if (p == null)
            throw new IllegalArgumentException();
        if (!points.contains(p))
            root = insert(root, p.x(), p.y(), 0, 0, 0, 1, 1);
    }

    private Node insert(Node n, double x, double y, int level,
                        double xmin, double ymin, double xmax, double ymax) {
        if (n == null) {
            Node nd = new Node(x, y);
            points.add(new Point2D(x, y));
            nd.rect = new RectHV(xmin, ymin, xmax, ymax);
            return nd;
        }
        if (level == 0) {
            if (x < n.p.x()) {
                xmax = n.p.x();
                n.lst = insert(n.lst, x, y, 1, xmin, ymin, xmax, ymax);
            }
            else { // (x >= n.p.x())
                xmin = n.p.x();
                n.rst = insert(n.rst, x, y, 1, xmin, ymin, xmax, ymax);
            }
        }
        else { // level = 1
            if (y < n.p.y()) {
                ymax = n.p.y();
                n.lst = insert(n.lst, x, y, 0, xmin, ymin, xmax, ymax);
            }
            else { // (y >= n.p.y())
                ymin = n.p.y();
                n.rst = insert(n.rst, x, y, 0, xmin, ymin, xmax, ymax);
            }
        }
        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root, 0);
    }

    private void drawNode(Node n, int level) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        int nxtLvl = (level == 0) ? 1 : 0;
        n.p.draw();
        if (level == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }
        else { // Level =1
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        if (n.lst != null) drawNode(n.lst, nxtLvl);
        if (n.rst != null) drawNode(n.rst, nxtLvl);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (isEmpty())
            return null;
        if (rect == null)
            throw new IllegalArgumentException();
        rectPoints = new ArrayList<Point2D>();
        search(root, rect);
        return rectPoints;
    }

    private void search(Node n, RectHV rect) {
        if (rect.intersects(n.rect)) {
            if (rect.contains(n.p))
                rectPoints.add(n.p);
            if (n.lst != null) search(n.lst, rect);
            if (n.rst != null) search(n.rst, rect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        if (p == null)
            throw new IllegalArgumentException();
        nearest = null;
        distSqr = 5;
        searchPoint(root, p, 0);
        return nearest;
    }

    private void searchPoint(Node n, Point2D p, int level) {
        double x = p.distanceSquaredTo(n.p);
        if (x < distSqr) {
            distSqr = p.distanceSquaredTo(n.p);
            nearest = n.p;
        }
        if (level == 0) {
            if (p.x() < n.p.x()) {
                searchChild(n.lst, p, level);
                searchChild(n.rst, p, level);
            }
            else {
                searchChild(n.rst, p, level);
                searchChild(n.lst, p, level);
            }
        }
        if (level == 1) {
            if (p.y() < n.p.y()) {
                searchChild(n.lst, p, level);
                searchChild(n.rst, p, level);
            }
            else {
                searchChild(n.rst, p, level);
                searchChild(n.lst, p, level);
            }
        }

    }

    private void searchChild(Node n, Point2D p, int level) {
        int nxtlvl = level == 0 ? 1 : 0;
        if (n != null) {
            if (n.rect.distanceSquaredTo(p) < distSqr) {
                searchPoint(n, p, nxtlvl);
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));
        kdtree.draw();
        Point2D qp = new Point2D(0.68, 0.34);
        System.out.println(kdtree.root.rect.distanceSquaredTo(qp));
        qp.draw();
        RectHV rect = new RectHV(0.0, 0.25, 0.25, 0.75);
        System.out.println(kdtree.range(rect));
        System.out.println(qp.distanceSquaredTo(new Point2D(0.2, 0.3)));
        System.out.println(qp.distanceSquaredTo(new Point2D(0.4, 0.7)));
        System.out.println(kdtree.nearest(qp));
    }
}
