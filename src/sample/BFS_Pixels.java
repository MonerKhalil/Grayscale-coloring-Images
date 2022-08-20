package sample;


import java.util.LinkedList;
import java.util.Queue;

public class BFS_Pixels {

    public int sensitivity = 200000;

    private static int validCoord(int x, int y, int n, int m) {
        if (x < 0 || y < 0) {
            return 0;
        }
        if (x >= n || y >= m) {
            return 0;
        }
        return 1;
    }
    public void bfs(int n, int m, int[][] data, int x, int y, int r, int g, int b,int s) {
        int[][] vis = new int[n][m];
        sensitivity = s;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vis[i][j] = 0;
            }
        }
        if(x>n-1) {
            x=n-1;
        }
        if(x<0) {
            x=0;
        }
        if (y>m-1){
            y = m-1;
        }
        if(y<0) {
            y=0;
        }
        Queue<Pair> obj = new LinkedList<>();
        Pair pq = new Pair(x, y);
        obj.add(pq);
        vis[x][y] = 1;
        while (!obj.isEmpty()) {
            Pair coord = obj.peek();
            int x1 = coord.first;
            int y1 = coord.second;
            int preColor = data[x1][y1];

            data[x1][y1] = 65536 * r + 256 * g + b;
            obj.remove();
            if ((validCoord(x1 + 1, y1, n, m) == 1) && vis[x1 + 1][y1] == 0 &&
                    (data[x1 + 1][y1] >= preColor - sensitivity && data[x1 + 1][y1] <= preColor + sensitivity)) {
                Pair p = new Pair(x1 + 1, y1);
                obj.add(p);
                vis[x1 + 1][y1] = 1;
            }
            if ((validCoord(x1 - 1, y1, n, m) == 1) && vis[x1 - 1][y1] == 0
                    && (data[x1 - 1][y1] >= preColor - sensitivity && data[x1 - 1][y1] <= preColor + sensitivity)) {
                Pair p = new Pair(x1 - 1, y1);
                obj.add(p);
                vis[x1 - 1][y1] = 1;
            }
            if ((validCoord(x1, y1 + 1, n, m) == 1) && vis[x1][y1 + 1] == 0 &&
                    (data[x1][y1 + 1] >= preColor - sensitivity && data[x1][y1 + 1] <= preColor + sensitivity)) {

                Pair p = new Pair(x1, y1 + 1);
                obj.add(p);
                vis[x1][y1 + 1] = 1;
            }
            if ((validCoord(x1, y1 - 1, n, m) == 1) && vis[x1][y1 - 1] == 0 && (data[x1][y1 - 1] >= preColor - sensitivity && data[x1][y1 - 1] <= preColor + sensitivity)) {
                Pair p = new Pair(x1, y1 - 1);
                obj.add(p);
                vis[x1][y1 - 1] = 1;
            }
        }
    }
}

class Pair implements Comparable<Pair> {
    int first;
    int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair o) {
        return second - o.second;
    }
}