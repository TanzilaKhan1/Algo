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
}

class BFSandDFS{
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

public class HelpClotilde{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            int O = sc.nextInt();
            int D = sc.nextInt();
            int K = sc.nextInt();
            if (O == 0 && D == 0 && K == 0) {
                break;
            }

            Set<Integer> forbidden = new HashSet<>();
            for (int i = 0; i < K; i++) {
                forbidden.add(sc.nextInt());
            }

            // Initialize the graph
            int MAX_CHANNEL = 100000;
            Graph g = new Graph();
            g.initializeGraph(MAX_CHANNEL + 1, 0); // Channels from 0 to 100000

            // Perform BFS to find the minimum number of button presses
            int result = bfs(O, D, forbidden, MAX_CHANNEL);
            System.out.println(result);
        }
        sc.close();
    }

    public static int bfs(int O, int D, Set<Integer> forbidden, int MAX_CHANNEL) {
        int inf = 1000000;
        int[] distance = new int[MAX_CHANNEL + 1];
        Arrays.fill(distance, inf);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(O);
        distance[O] = 0;

        while (!queue.isEmpty()) {
            int c = queue.poll();

            if (c == D) {
                return distance[c];
            }

            if (forbidden.contains(c)) {
                continue;
            }

            // Possible moves
            int[] nextChannels = new int[5];
            int idx = 0;

            // +1
            if (c + 1 <= MAX_CHANNEL) nextChannels[idx++] = c + 1;

            // -1
            if (c - 1 >= 0) nextChannels[idx++] = c - 1;

            // x2
            if (c * 2 <= MAX_CHANNEL) nextChannels[idx++] = c * 2;

            // x3
            if (c * 3 <= MAX_CHANNEL) nextChannels[idx++] = c * 3;

            // /2
            if (c % 2 == 0 && c / 2 >= 0) nextChannels[idx++] = c / 2;

            for (int i = 0; i < idx; i++) {
                int n = nextChannels[i];
                if (!forbidden.contains(n) && distance[n] == inf) {
                    distance[n] = distance[c] + 1;
                    queue.add(n);
                }
            }
        }

        return -1; // Destination not reachable
    }
}
