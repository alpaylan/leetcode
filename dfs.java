
/// Find your way through the board
// Board is 9x9 squares, where there is a tile between each square
// The coordinate system is as follows
// There are 17 x-y coordinates in total, numbered from 0 to 16
// Squares are at (even, even) coordinates
// Tiles are at (odd, even) or (even, even) coordinates
// Objective is to find a path from a given square to some (x=2n, 0)
// The pawn can move to any of the 4 adjacent squares, but cannot move over a tile

import java.util.ArrayList;
import java.util.List;

public class dfs {
    public static class Point {
        public int x;
        public int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Board {
        public Point[] tiles;
        public Point start;
        public static int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        public static int LENGTH = 17;
        public Point[] path = null;
        public Board(Point[] tiles, Point start) {
            this.tiles = tiles;
            this.start = start;
        }

        public void addPath(Point[] path) {
            this.path = path;
        }
        public boolean hasTile(int x, int y) {
            for (Point tile : tiles) {
                if (tile.x == x && tile.y == y) {
                    return true;
                }
            }
            return false;
        }

        public static Point[] removeTile(Point[] tiles, Point tile) {
            List<Point> result = new ArrayList<>();
            for (Point t : tiles) {
                if (t.x != tile.x || t.y != tile.y) {
                    result.add(t);
                }
            }
            return result.toArray(new Point[result.size()]);
        }

        public boolean inPath(int x, int y) {
            if (path == null) {
                return false;
            }
            for (Point p : path) {
                if (p.x == x && p.y == y) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasTile(Point point) {
            return hasTile(point.x, point.y);
        }

        public boolean isSquare(int x, int y) {
            return x % 2 == 0 && y % 2 == 0;
        }

        public boolean isSquare(Point point) {
            return isSquare(point.x, point.y);
        }

        public boolean isTile(int x, int y) {
            return !isSquare(x, y);
        }

        public boolean isTile(Point point) {
            return isTile(point.x, point.y);
        }

        public boolean isOnBoard(int x, int y) {
            return x >= 0 && x < LENGTH && y >= 0 && y < LENGTH;
        }

        public boolean isOnBoard(Point point) {
            return isOnBoard(point.x, point.y);
        }

        public boolean canMove(Point p1, int[] direction) {
            Point targetPoint = new Point(p1.x + direction[0] * 2, p1.y + direction[1] * 2);
            Point middlePoint = new Point(p1.x + direction[0], p1.y + direction[1]);

            return isOnBoard(targetPoint) && isSquare(targetPoint) && !hasTile(middlePoint);
        }
        
        public void printBoard() {
            for (int i = 0; i < LENGTH; i++) {
                for (int j = 0; j < LENGTH; j++) {
                    if (start.x == i && start.y == j) {
                        System.out.print("o");
                    } else if (inPath(i, j)) {
                        System.out.print("x");
                    } else if (isSquare(i, j)) {
                        System.out.print('\u25A6');
                    } 
                    else if (hasTile(i, j) && i % 2 == 1) {
                        System.out.print('\u2501');
                    } else if (isTile(i, j) && i % 2 == 1) {
                        System.out.print('\u2504');
                    } else if (hasTile(i, j) && j % 2 == 1) {
                        System.out.print('\u2503');
                    } else if (isTile(i, j) && j % 2 == 1) {
                        System.out.print('\u2506');
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }

        public boolean dfs_false(Point start, Point end) {
            if (start.x == end.x && start.y == end.y) {
                return true;
            }
            for (int[] direction : DIRECTIONS) {
                if (canMove(start, direction)) {
                    Point nextPoint = new Point(start.x + direction[0] * 2, start.y + direction[1] * 2);
                    if (dfs_false(nextPoint, end)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean dfs(Point start, Point end, boolean[][] visited) {
            if (start.x == end.x && start.y == end.y) {
                return true;
            }
            for (int[] direction : DIRECTIONS) {
                if (canMove(start, direction)) {
                    Point nextPoint = new Point(start.x + direction[0] * 2, start.y + direction[1] * 2);
                    if (!visited[nextPoint.x][nextPoint.y]) {
                        visited[nextPoint.x][nextPoint.y] = true;
                        if (dfs(nextPoint, end, visited)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public boolean dfs(Point start, Point end) {
            boolean[][] visited = new boolean[LENGTH][LENGTH];
            visited[start.x][start.y] = true;
            return dfs(start, end, visited);
        }

        public Point[] dfs_tracking (Point start, Point end, boolean[][] visited) {
            if (start.x == end.x && start.y == end.y) {
                return new Point[] {start};
            }
            for (int[] direction : DIRECTIONS) {
                if (canMove(start, direction)) {
                    Point nextPoint = new Point(start.x + direction[0] * 2, start.y + direction[1] * 2);
                    if (!visited[nextPoint.x][nextPoint.y]) {
                        visited[nextPoint.x][nextPoint.y] = true;
                        Point[] path = dfs_tracking(nextPoint, end, visited);
                        if (path != null) {
                            Point[] result = new Point[path.length + 1];
                            result[0] = start;
                            for (int i = 0; i < path.length; i++) {
                                result[i + 1] = path[i];
                            }
                            return result;
                        }
                    }
                }
            }
            return null;
        }

        public Point[] dfs_tracking (Point start, Point end) {
            boolean[][] visited = new boolean[LENGTH][LENGTH];
            visited[start.x][start.y] = true;
            return dfs_tracking(start, end, visited);
        }
    }

    public static Board genBoard() {
        /// generate a random board

        // generate random tiles
        int numTiles = ((int) (Math.random() * 50)) + 50;
        Point[] tiles = new Point[numTiles];
        for (int i = 0; i < numTiles; i++) {
            int x = (int) (Math.random() * 8);
            int y = (int) (Math.random() * 8);
            int orient = (int) (Math.random() * 2);
            if (orient == 0) {
                tiles[i] = new Point(x * 2 + 1, y * 2);
            } else {
                tiles[i] = new Point(x * 2, y * 2 + 1);
            }
        }

        // generate random start
        int x = ((int) (Math.random() * 8)) * 2;
        int y = ((int) (Math.random() * 8)) * 2;

        return new Board(tiles, new Point(x, y));
    }

    public static boolean checkPathCorrectness(Board board, Point[] path) {
        if (path == null) {
            return false;
        }
        for (int i = 0; i < path.length - 1; i++) {
            int x1 = path[i].x;
            int y1 = path[i].y;
            int x2 = path[i + 1].x;
            int y2 = path[i + 1].y;
            if (x1 == x2) {
                if (y1 + 2 == y2) {
                    if (board.hasTile(x1, y1 + 1)) {
                        return false;
                    }
                } else if (y1 - 2 == y2) {
                    if (board.hasTile(x1, y1 - 1)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (y1 == y2) {
                if (x1 + 2 == x2) {
                    if (board.hasTile(x1 + 1, y1)) {
                        return false;
                    }
                } else if (x1 - 2 == x2) {
                    if (board.hasTile(x1 - 1, y1)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static Point[] createRandomWalk(Point start, Point target) {
        int xDir = target.x == start.x ? 0 : target.x > start.x ? 2 : -2;
        int yDir = target.y == start.y ? 0 : target.y > start.y ? 2 : -2;

        if (xDir == 0 && yDir == 0) {
            return new Point[] {start};
        }

        int x = start.x;
        int y = start.y;

        List<Point> path = new ArrayList<>();

        while (x != target.x || y != target.y) {
            if (x != target.x && y != target.y) {
                if (Math.random() < 0.5) {
                    x += xDir;
                } else {
                    y += yDir;
                }
            } else if (x != target.x) {
                x += xDir;
            } else {
                y += yDir;
            }
            path.add(new Point(x, y));
        }

        return path.toArray(new Point[path.size()]);
    }
    public static Board crushTilesOnTheWay(Board board, Point[] path) {
        for (int i = 0; i < path.length - 1; i++) {
            int x1 = path[i].x;
            int y1 = path[i].y;
            int x2 = path[i + 1].x;
            int y2 = path[i + 1].y;
            if (x1 == x2) {
                if (y1 + 2 == y2) {
                    if (board.hasTile(x1, y1 + 1)) {
                        System.out.println("Crushing tile at " + x1 + " " + (y1 + 1));
                        board.tiles = Board.removeTile(board.tiles, new Point(x1, y1 + 1));
                    }
                } else if (y1 - 2 == y2) {
                    if (board.hasTile(x1, y1 - 1)) {
                        System.out.println("Crushing tile at " + x1 + " " + (y1 - 1));
                        board.tiles = Board.removeTile(board.tiles, new Point(x1, y1 - 1));
                    }
                }
            } else if (y1 == y2) {
                if (x1 + 2 == x2) {
                    if (board.hasTile(x1 + 1, y1)) {
                        System.out.println("Crushing tile at " + (x1 + 1) + " " + y1);
                        board.tiles = Board.removeTile(board.tiles, new Point(x1 + 1, y1));
                    }
                } else if (x1 - 2 == x2) {
                    if (board.hasTile(x1 - 1, y1)) {
                        System.out.println("Crushing tile at " + (x1 - 1) + " " + y1);
                        board.tiles = Board.removeTile(board.tiles, new Point(x1 - 1, y1));
                    }
                }
            }
        }
        return board;
    }

    public static void main (String[] args) {
        Board board = genBoard();
        board.printBoard();
        System.out.println("=================");
        Point[] path = board.dfs_tracking(board.start, new Point(0, 0));
        if (path != null) {
            board.addPath(path);
            board.printBoard();
            System.out.println("=================");
            System.out.println(checkPathCorrectness(board, path));
        } else {
            System.out.println("No path found");
            System.out.println("=================");
            Point[] walk = createRandomWalk(board.start, new Point(0, 0));
            board = crushTilesOnTheWay(board, walk);
            board.printBoard();
            System.out.println("=================");
            System.out.println(board.dfs(board.start, new Point(0, 0)));
            board.addPath(walk);
            System.out.println("=================");
            board.printBoard();


        }

    }
}
