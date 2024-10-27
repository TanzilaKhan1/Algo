import java.io.*;
import java.util.*;

class Graph {
    int verticeNo;
    int edgeNo;
    LinkedList<LinkedList<Integer>> adjacencyList;

    Graph() {
        adjacencyList = new LinkedList<>();
    }

    public void initializeGraph(int vertices, int edges) {
        this.verticeNo = vertices;
        this.edgeNo = edges;
        adjacencyList.clear();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new LinkedList<>());
        }
    }

    public void addEdge(int u, int v) {
        if (u < 0 || u >= this.verticeNo || v < 0 || v >= this.verticeNo) {
            // System.err.println("Invalid edge: " + u + " -> " + v);
            return;
        }
        adjacencyList.get(u).add(v);
    }

    public void displayGraph() {
        for (int i = 0; i < verticeNo; i++) {
            System.out.print("Vertex " + i + ": "); // 0-based indexing
            for (Integer neighbor : adjacencyList.get(i)) {
                System.out.print(neighbor + " ");  // 0-based indexing
            }
            System.out.println();
        }
    }
}

class BFSandDFS {
    Graph g;
    String[] color;
    int[] prev, distance;
    int inf = 1000000;
    List<Integer> discoveryOrder = new ArrayList<>();  // To store the order of discovery

    BFSandDFS(Graph graph) {
        this.g = graph;
        this.color = new String[this.g.verticeNo];
        this.prev = new int[this.g.verticeNo];
        this.distance = new int[this.g.verticeNo];
    }

    public void startBFS(int startVer) {
        discoveryOrder.clear();
        for (int i = 0; i < this.g.verticeNo; i++) {
            color[i] = "WHITE";
            prev[i] = -1;
            distance[i] = inf;
        }
        color[startVer] = "GRAY";
        distance[startVer] = 0;
        prev[startVer] = -1;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVer);
        discoveryOrder.add(startVer);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : this.g.adjacencyList.get(u)) {
                if (color[v].equals("WHITE")) {
                    color[v] = "GRAY";
                    distance[v] = distance[u] + 1;
                    prev[v] = u;
                    queue.add(v);
                    discoveryOrder.add(v);
                }
            }
            color[u] = "BLACK";
        }
    }
}

public class Grid {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt(); // Number of columns
        int n = sc.nextInt(); // Number of rows
        sc.nextLine(); // Consume the rest of the line

        int[][] grid = new int[n][m];
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < m; j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }

        int totalVertices = n * m;
        Graph g = new Graph();
        g.initializeGraph(totalVertices, 0);

        // Build the graph
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int k = grid[i][j];
                if (k == 0) {
                    continue; // Cannot move from a cell with 0
                }
                int u = i * m + j; // Convert 2D index to 1D

                // Up
                if (i - k >= 0) {
                    int v = (i - k) * m + j;
                    g.addEdge(u, v);
                }
                // Down
                if (i + k < n) {
                    int v = (i + k) * m + j;
                    g.addEdge(u, v);
                }
                // Left
                if (j - k >= 0) {
                    int v = i * m + (j - k);
                    g.addEdge(u, v);
                }
                // Right
                if (j + k < m) {
                    int v = i * m + (j + k);
                    g.addEdge(u, v);
                }
            }
        }

        // Perform BFS
        BFSandDFS bfs = new BFSandDFS(g);
        int startVertex = 0; // Top-left corner
        int endVertex = totalVertices - 1; // Bottom-right corner

        bfs.startBFS(startVertex);

        if (bfs.distance[endVertex] != bfs.inf) {
            System.out.println(bfs.distance[endVertex]);
        } else {
            System.out.println("-1");
        }

        sc.close();
    }
}
