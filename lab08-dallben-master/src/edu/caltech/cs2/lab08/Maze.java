package edu.caltech.cs2.lab08;

import edu.caltech.cs2.libraries.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
    public int n;                 // dimension of maze
    public boolean[][] north;     // is there a wall to north of cell i, j
    public boolean[][] east;
    public boolean[][] south;
    public boolean[][] west;
    public boolean done = false;
    public Point end;
    private static final int DRAW_WAIT = 1;

    public Maze(int n, String mazeFile) throws FileNotFoundException {
        this.n = n;
        end = new Point(n / 2, n / 2);
        StdDraw.setXscale(0, n + 2);
        StdDraw.setYscale(0, n + 2);
        init();

        Scanner scanner = new Scanner(new File(mazeFile));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            assert tokens.length == 3;
            String direction = tokens[0];
            int x = Integer.valueOf(tokens[1]);
            int y = Integer.valueOf(tokens[2]);
            switch (direction) {
                case "N":
                    north[x][y] = false;
                    break;
                case "S":
                    south[x][y] = false;
                    break;
                case "E":
                    east[x][y] = false;
                    break;
                case "W":
                    west[x][y] = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void init() {
        // initialze all walls as present
        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }

    // draw the maze
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(n / 2.0 + 0.5, n / 2.0 + 0.5, 0.375);
        StdDraw.filledCircle(1.5, 1.5, 0.375);

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x, y, x + 1, y);
                if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
                if (west[x][y]) StdDraw.line(x, y, x, y + 1);
                if (east[x][y]) StdDraw.line(x + 1, y, x + 1, y + 1);
            }
        }
        StdDraw.show();
        StdDraw.pause(1000);
    }

    // Draws a blue circle at coordinates (x, y)
    private void selectPoint(Point point) {
        int x = point.x;
        int y = point.y;
        System.out.println("Selected point: (" + x + ", " + y + ")");
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(DRAW_WAIT);
    }

    /*
     * Returns an array of all children to a given point
     */
    public Point[] getChildren(Point point) {
        /* TODO */
        Point[] points = new Point[4];
        int x = point.x;
        int y = point.y;
        if(!north[x][y]){
            points[0] = new Point(x, y+1);
            points[0].parent = point;
        }
        if(!east[x][y]){
            points[1] = new Point(x+1, y);
            points[1].parent = point;
        }
        if(!west[x][y]){
            points[2] = new Point(x-1, y);
            points[2].parent = point;
        }
        if(!south[x][y]){
            points[3] = new Point(x, y-1);
            points[3].parent = point;
        }
        int count = 0;
        for(Point p: points){
            if(p != null){
                count++;
            }
        }
        Point[] result = new Point[count];
        int idx = 0;
        for(int i = 0; i < 4; i++){
            if(points[i] != null){
                result[idx] = points[i];
                idx++;
            }
        }
        return result;
    }

    public void solveDFSRecursiveStart() {
        Point start = new Point(1, 1);
        solveDFSRecursive(start);
    }

    /*
     * Solves the maze using a recursive DFS. Calls selectPoint()
     * when a point to move to is selected.
     */
    private void solveDFSRecursive(Point point) {
        /* TODO */
        if(point.isEqual(new Point(1, 1))){
            selectPoint(point);
        }
        if(point.isEqual(end)){
            //selectPoint(end);
            done = true;
        } else{
            Point[] children = getChildren(point);
            for(Point p: children){
                if(!p.isEqual(point.parent) && !done) {
                    selectPoint(p);
                    solveDFSRecursive(p);
                }
            }
        }
    }

    /*
     * Solves the maze using an iterative DFS using a stack. Calls selectPoint()
     * when a point to move to is selected.
     */
    public void solveDFSIterative() {
        /* TODO */
        Stack<Point> childs = new Stack<>();
        childs.push(new Point(1, 1));

        while(!childs.empty() && !done){
            Point point = childs.pop();
            selectPoint(point);
            if(point.isEqual(end)){
                done = true;
                break;
            }
            Point[] children = getChildren(point);
            for(Point p : children) {
                if (!p.isEqual(point.parent)){
                    childs.push(p);
                }
            }
        }
    }


}

