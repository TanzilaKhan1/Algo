import java.io.*;
import java.util.*;

class Graph {
    int verticeNo;
    int edgeNo;
    LinkedList<LinkedList<Integer>> adjacencyList;

    Graph() {
        adjacencyList = new LinkedList<>();
    }

    public void initializeGraph(int vertices) {
        this.verticeNo = vertices;
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
            System.out.print("Vertex " + (i+1) + ": "); // 1-based indexing
            System.out.print("Vertex " + i + ": "); // 0-based indexing
            for (Integer neighbor : adjacencyList.get(i)) {
                System.out.print((neighbor+1) + " "); // 1-based indexing
                System.out.print(neighbor + " ");  // 0-based indexing
            }
            System.out.println();
        }
    }

    public void readInput(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String[] ve = br.readLine().split(" ");
            verticeNo = Integer.parseInt(ve[0]);
            edgeNo = Integer.parseInt(ve[1]);

            initializeGraph(verticeNo);

            for (int i = 0; i < edgeNo; i++) {
                String[] edge = br.readLine().split(" ");
                int u = Integer.parseInt(edge[0]);
                int v = Integer.parseInt(edge[1]);

                addEdge(u, v);
                addEdge(u-1, v-1); // 1-based index graph
            }
        } catch (IOException ex) {
            System.err.println("Error Reading File: " + ex.getMessage());
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
        
        // Convert startVer to 0-based indexing from 1-based
        startVer = startVer - 1;
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

    public void DFS(int startVer){

        this.time = 0;
        discoveryOrder.clear(); // Reset the discovery order list
        finishingOrder.clear(); 

        for (int i = 0; i < this.g.verticeNo; i++) {  
            color[i] = "WHITE";
            prev[i] = -2;
            finish[i] = 0;
            discovery[i] = 0;
        }
        
        if (color[startVer].equals("WHITE")) {
            DFS_Visit(startVer);
        }

        for (int i = 0; i < this.g.verticeNo; i++) {
            if (color[i].equals("WHITE")) {
                DFS_Visit(i);
            }
        }
    }

    public void DFS_Visit(int u){

        color[u] = "GRAY";
        this.time = this.time + 1;  
        discovery[u] = this.time;
        discoveryOrder.add(u);  // Track discovery order
        
        for (int v : this.g.adjacencyList.get(u)) {
            if (color[v].equals("WHITE")) {
                prev[v] = u;
                DFS_Visit(v);
            }
        }
        
        color[u] = "BLACK";
        this.time = this.time + 1;
        finish[u] = this.time;
        finishingOrder.add(u);
    }

    public void PrintAllValuesForBFS(){// Output the result for color, prev, and distance
        System.out.println("\nVertex\tColor\tPredecessor\tDistance");
        for (int i = 0; i < this.g.verticeNo; i++) {
            //System.out.print((i + 1)+ "\t" + color[i] + "\t" + (prev[i] + 1)+ "\t" + distance[i]); // Output 0-based index for vertices  // Output 1-based index for vertices
            System.out.println(i + "\t" + color[i] + "\t" + prev[i]+ "\t" + distance[i]);// Output 0-based index for vertices
        }
    }

    public void PrintAllValuesForDFS(){// Output the result for color, prev, and distance
        System.out.println("\nVertex\tStart\tFinish");
        for (int i = 0; i < this.g.verticeNo; i++) {
            System.out.println(i + "\t" + discovery[i] + "\t" + finish[i]);
        }

    }

    public void PrintDiscoveryOrder(){
        System.out.println("\nDiscovery Order:");
        for (int i = 0; i < discoveryOrder.size(); i++) {
            System.out.print(discoveryOrder.get(i) + " ");
        }
        System.out.println();
    }
        
    public void PrintFinishingOrder(){
        System.out.println("\nFinishing Order:");
        for (int i = 0; i < finishingOrder.size(); i++) {
            System.out.print(finishingOrder.get(i) + " ");
        }
        System.out.println();
    }
    
    public void PrintShortestPath(int startVer) { // Print shortest path for each vertex from startVer
        System.out.println("Shortest Paths from Vertex " + startVer + ":");
        for (int i = 0; i < this.g.verticeNo; i++) {
            if (i != startVer) {
                System.out.print("Path to " + i + ": ");
                printPath(i);
                System.out.println();
            }
        }
    }

    public void printPath(int vertex) {
        if (prev[vertex] == -1 || prev[vertex] == -2) {
            System.out.print(vertex);
        } else {
            printPath(prev[vertex]);
            System.out.print(" -> " + vertex);
        }
    }
}

class TopologicalSort{
    TopologicalSort(Graph graph){
        BFSandDFS graphTraversal = new BFSandDFS(graph);
        graphTraversal.DFS(0);
        System.out.println("\nTopological Sort: ");
        for (int i = graphTraversal.finishingOrder.size() - 1; i >= 0; i--) {
            System.out.print(graphTraversal.finishingOrder.get(i)+" ");
        }
        System.out.println();
    }
}
class StronglyConnectedComponents{
    //kosaraju's algorithm

    Graph graph;  // The original graph
    Graph transposedGraph;  // The transposed graph
    BFSandDFS dfsTraversal;  // DFS object for the original graph
    List<List<Integer>> sccList;  // List to store all the SCCs
    
    StronglyConnectedComponents(Graph graph) {
        this.graph = graph;
        this.transposedGraph = new Graph();  // We will build the transposed graph later
        this.dfsTraversal = new BFSandDFS(graph);  // We will use the DFS class for Kosaraju's algorithm
        this.sccList = new ArrayList<>();
        findSCCs();  // Call the method to find the SCCs
    }
    
    // Method to find all the SCCs
    public void findSCCs() {
        // Step 1: Perform DFS on the original graph to calculate finishing times
        dfsTraversal.DFS(0);  // Start DFS from vertex 0 (you can generalize this later)
        // Step 2: Transpose the graph (reverse the edges)
        transposeGraph();
        // Step 3: Perform DFS on the transposed graph in the order of decreasing finishing times
        boolean[] visited = new boolean[graph.verticeNo];
        Arrays.fill(visited, false);
    
        // Use the finishingOrder from the original DFS
        for (int i = dfsTraversal.finishingOrder.size() - 1; i >= 0; i--) {
            int vertex = dfsTraversal.finishingOrder.get(i);
            if (!visited[vertex]) {
                List<Integer> scc = new ArrayList<>();
                DFSOnTransposedGraph(vertex, visited, scc);  // DFS on the transposed graph
                sccList.add(scc);  // Add the found SCC to the list
            }
        }
    
        // Step 4: Print all the SCCs
        printSCCs();
    }
    
    // Method to perform DFS on the transposed graph
    private void DFSOnTransposedGraph(int vertex, boolean[] visited, List<Integer> scc) {
        visited[vertex] = true;
        scc.add(vertex);
    
        for (int neighbor : transposedGraph.adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                DFSOnTransposedGraph(neighbor, visited, scc);
            }
        }
    }
    
    // Method to transpose the graph (reverse all edges)
    private void transposeGraph() {
        transposedGraph.initializeGraph(graph.verticeNo);  // Initialize the transposed graph with the same number of vertices
    
        for (int u = 0; u < graph.verticeNo; u++) {
            for (int v : graph.adjacencyList.get(u)) {
                transposedGraph.addEdge(v, u);  // Reverse the edge u -> v to v -> u
            }
        }
    }
    
    // Method to print all the SCCs
    private void printSCCs() {
        System.out.println("Strongly Connected Components:");
        for (int i = 0; i < sccList.size(); i++) {
            System.out.println("SCC " + (i + 1) + ": " + sccList.get(i));
        }
    }
}


public class GraphAlgos {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the input file path: ");
        String path = sc.nextLine();

        Graph graph = new Graph();
        graph.readInput(path);
        BFSandDFS traversal = new BFSandDFS(graph);

        // Perform DFS with start/finish times
        System.out.println("\nBFS Traversal:");
        traversal.startBFS(0);

        // Perform BFS
        System.out.println("\nDFS Traversal:");
        traversal.DFS(0);

        // Perform Topological Sort
        TopologicalSort topologicalSort = new TopologicalSort(graph);

        // Find Strongly Connected Components
        System.out.println("\nStrongly Connected Components:");
        
        sc.close();
    }
}
