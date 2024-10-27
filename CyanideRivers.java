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
        this.edgeNo= edges;
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
            System.out.print("Vertex " + i + ": "); // 0-based indexing
            for (Integer neighbor : adjacencyList.get(i)) {
                System.out.print(neighbor + " ");  // 0-based indexing
            }
            System.out.println();
        }
    }
}

class BFSandDFS{
    Graph g;
    String[] color;
    int[] prev, distance, discovery, finish;
    int inf = 1000000, time=0;
    List<Integer> discoveryOrder = new ArrayList<>();  // To store the order of discovery
    List<Integer> finishingOrder= new ArrayList<>();

    BFSandDFS(Graph graph) {
        this.g= graph;
        this.color = new String[this.g.verticeNo];
        this.prev = new int[this.g.verticeNo];
        this.distance = new int[this.g.verticeNo];
        this.discovery = new int[this.g.verticeNo];
        this.finish = new int[this.g.verticeNo];
    }

    public void startBFS(int startVer) {
        discoveryOrder.clear();
        finishingOrder.clear();
        for (int i = 0; i < this.g.verticeNo; i++) {  
            color[i] = "WHITE";
            prev[i] = -2;
            distance[i] = inf;
        }
        color[startVer] = "GRAY";
        distance[startVer] = 0;
        prev[startVer] = -2;

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
            finishingOrder.add(u);
        }

    } 

    // Other methods remain unchanged (not necessary for this problem)
}

public class CyanideRivers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        int N = input.length();
        Graph g = new Graph();
        g.initializeGraph(N, 0);

        // Build the graph: add edges between adjacent towers
        for (int i = 0; i < N; i++) {
            if (i > 0) {
                g.addEdge(i, i - 1);
            }
            if (i < N - 1) {
                g.addEdge(i, i + 1);
            }
        }

        // Identify the positions of '1's (shore or island towers)
        List<Integer> startVertices = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (input.charAt(i) == '1') {
                startVertices.add(i);
            }
        }

        BFSandDFS bfs = new BFSandDFS(g);

        // Since we cannot modify the template's startBFS method, we perform BFS for each starting vertex
        // and keep track of the minimum distance
        int[] minDays = new int[N];
        Arrays.fill(minDays, bfs.inf);

        for (int startVer : startVertices) {
            bfs.startBFS(startVer);
            for (int i = 0; i < N; i++) {
                if (bfs.distance[i] < minDays[i]) {
                    minDays[i] = bfs.distance[i];
                }
            }
        }

        // Find the maximum day among all towers
        int maxDay = 0;
        for (int i = 0; i < N; i++) {
            if (minDays[i] != bfs.inf) {
                maxDay = Math.max(maxDay, minDays[i]);
            }
        }

        System.out.println(maxDay);
        sc.close();
    }
}
