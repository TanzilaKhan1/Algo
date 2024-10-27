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
            System.err.println("Invalid edge: " + u + " -> " + v);
            return;
        }
        adjacencyList.get(u).add(v);
    }

    public void displayGraph() {
        for (int i = 0; i < verticeNo; i++) {  
            System.out.print("Vertex " + (i + 1) + ": "); // 1-based indexing
            for (Integer neighbor : adjacencyList.get(i)) {
                System.out.print((neighbor + 1) + " "); // 1-based indexing
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

    BFSandDFS(Graph graph) {
        this.g = graph;
        this.color = new String[this.g.verticeNo];
        this.prev = new int[this.g.verticeNo];
        this.distance = new int[this.g.verticeNo];
    }

    public void startBFS(int startVer) {
        Arrays.fill(color, "WHITE");
        Arrays.fill(prev, -1);
        Arrays.fill(distance, inf);
        
        color[startVer] = "GRAY";
        distance[startVer] = 0;
        prev[startVer] = -1;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVer);
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : this.g.adjacencyList.get(u)) {
                if (color[v].equals("WHITE")) {
                    color[v] = "GRAY";
                    distance[v] = distance[u] + 1;
                    prev[v] = u;
                    queue.add(v);
                }
            }
            color[u] = "BLACK";
        }
    }
}

public class KnightMoves {
    // Possible knight moves
    static final int[][] moves = {
        {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
        {-1, -2}, {1, -2}, {-1, 2}, {1, 2}
    };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
            String[] parts = line.split(" ");
            String startSquare = parts[0];
            String endSquare = parts[1];

            int startRow = startSquare.charAt(1) - '1'; // Convert row
            int startCol = startSquare.charAt(0) - 'a'; // Convert column
            int endRow = endSquare.charAt(1) - '1';     // Convert row
            int endCol = endSquare.charAt(0) - 'a';     // Convert column

            // Create a graph with 64 vertices (for each square on the chessboard)
            Graph g = new Graph();
            g.initializeGraph(64, 0);

            // Generate edges based on knight's moves
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    int vertex = row * 8 + col;
                    for (int[] move : moves) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];
                        if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                            g.addEdge(vertex, newRow * 8 + newCol);
                        }
                    }
                }
            }

            BFSandDFS bfs = new BFSandDFS(g);
            bfs.startBFS(startRow * 8 + startCol);
            int distance = bfs.distance[endRow * 8 + endCol];

            // Print result
            System.out.printf("To get from %s to %s takes %d knight moves.%n", startSquare, endSquare, distance);
        }
    }
}
