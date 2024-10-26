import java.io.*;
import java.util.*;

class Graph {
    int vertice_no;
    int edge_no;
    LinkedList<LinkedList<Integer>> adjacencyList;
    String[] color;
    int[] prev ,finish ,discovery, distance;
    int inf = 1000000;
    int time=0;
    List<Integer> discoveryOrder = new ArrayList<>();  // To store the order of discovery
    List<Integer> finishingOrder= new ArrayList<>();

    Graph(Scanner scanner) {
        this.vertice_no = scanner.nextInt();
        this.edge_no = scanner.nextInt();
        this.adjacencyList = new LinkedList<>();
        this.color = new String[this.vertice_no];
        this.prev = new int[this.vertice_no];
        this.finish = new int[this.vertice_no];
        this.discovery = new int[this.vertice_no];
        this.distance = new int[this.vertice_no];
       // this.finishingOrder = new ArrayList<>();
       // List<Integer> discoveryOrder = new ArrayList<>();
        for (int i = 0; i < vertice_no; i++) {  
            adjacencyList.add(new LinkedList<>());
        }
        drawEdge(this.edge_no, scanner);
    }

    public void drawEdge(int edge_no, Scanner scanner) {
        int u, v;
        for (int i = 0; i < edge_no; i++) {  
            u = scanner.nextInt();
            v = scanner.nextInt();
            addEdge(u, v);
        }
    }

    public void addEdge(int u, int v) {
        if (u < 0 || u > this.vertice_no || v < 0 || v > this.vertice_no) {
            System.err.println("Invalid edge: " + u + " -> " + v);
            return;
        }
       // adjacencyList.get(u-1).add(v-1); // 0-based index from 1-based for vertices
        adjacencyList.get(u).add(v); // 0-based
    }

    public void addNewVertexAndEdge(int new_vertice, int new_edge, Scanner scanner) {
        for (int i = 0; i < new_vertice; i++) {
            adjacencyList.add(new LinkedList<>());
        }
        vertice_no += new_vertice;
        edge_no += new_edge;
        drawEdge(new_edge, scanner);
    }

    public void displayGraph() {
        for (int i = 0; i < vertice_no; i++) {  
            //System.out.print("Vertex " + (i+1) + ": "); // 1-based indexing
            System.out.print("Vertex " + i + ": "); // 0-based indexing
            for (Integer neighbor : adjacencyList.get(i)) {
                //System.out.print((neighbor+1) + " "); // 1-based indexing
                System.out.print(neighbor + " ");  // 0-based indexing
            }
            System.out.println();
        }
    }

    public void BFS(int startVer) {
        
        // Convert startVer to 0-based indexing from 1-based
        //startVer = startVer - 1;
        discoveryOrder.clear();
        for (int i = 0; i < vertice_no; i++) {  
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
            for (int v : adjacencyList.get(u)) {
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

        // Output the result for color, prev, and distance
        System.out.println("\nVertex\tColor\tPredecessor\tDistance");
        for (int i = 0; i < vertice_no; i++) {
            //System.out.print((i + 1)+ "\t" + color[i] + "\t" + (prev[i] + 1)+ "\t" + distance[i]); // Output 0-based index for vertices  // Output 1-based index for vertices
            System.out.println(i + "\t" + color[i] + "\t" + prev[i]+ "\t" + distance[i]);// Output 0-based index for vertices
        }

        // Print discovery order
        System.out.println("\nDiscovery Order:");
        for (int i = 0; i < discoveryOrder.size(); i++) {
            System.out.print(discoveryOrder.get(i) + " ");
        }
        System.out.println();
        
        // Print shortest path for each vertex from startVer
        System.out.println("Shortest Paths from Vertex " + startVer + ":");
        for (int i = 0; i < vertice_no; i++) {
            if (i != startVer) {
                System.out.print("Path to " + i + ": ");
                printPath(i);
                System.out.println();
            }
        }
    }

    public void DFS(int startVer){

        this.time = 0;
        discoveryOrder.clear(); // Reset the discovery order list
        finishingOrder.clear(); 

        for (int i = 0; i < this.vertice_no; i++) {  
            color[i] = "WHITE";
            prev[i] = -2;
            finish[i] = 0;
            discovery[i] = 0;
        }
        
        if (color[startVer].equals("WHITE")) {
            DFS_Visit(startVer);
        }

        for (int i = 0; i < this.vertice_no; i++) {
            if (color[i].equals("WHITE")) {
                DFS_Visit(i);
            }
        }

        System.out.println("\nVertex\tStart\tFinish");
        for (int i = 0; i < vertice_no; i++) {
            System.out.println(i + "\t" + discovery[i] + "\t" + finish[i]);
        }

        // Print discovery order
        System.out.println("\nDiscovery Order: ");
        for (int i = 0; i < discoveryOrder.size(); i++) {
            System.out.print(discoveryOrder.get(i) + " ");
        }
        System.out.println();

        System.out.println("Finishing Order:");
        for (int v : finishingOrder) {
            System.out.print(v + " ");
        }
        System.out.println();


        // Print the paths
        System.out.println("\nDFS Paths from Vertex " + startVer + ":");
        for (int i = 0; i < vertice_no; i++) {
            if (i != startVer) {
                System.out.print("Path to " + i + ": ");
                printPath(i);
                System.out.println();
            }
        }
    }

    public void DFS_Visit(int u){

        color[u] = "GRAY";
        this.time = this.time + 1;  
        discovery[u] = this.time;
        discoveryOrder.add(u);  // Track discovery order
        
        for (int v : this.adjacencyList.get(u)) {
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

    // Utility to print the path from startVer to a given vertex
    public void printPath(int vertex) {
        if (prev[vertex] == -1 || prev[vertex] == -2) {
            System.out.print(vertex);
        } else {
            printPath(prev[vertex]);
            System.out.print(" -> " + vertex);
        }
    }


    public void TopologicalSort(){
        DFS(0);
        System.out.println("\nTopological Sort: ");
        for (int i = finishingOrder.size() - 1; i >= 0; i--) {
            System.out.print(finishingOrder.get(i)+" ");
        }
        System.out.println();
    }
}

public class Graph_Traversal {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Graph graph = new Graph(scan);
        int startVer = scan.nextInt();  // Input 0-based index for the start vertex
        //graph.BFS(startVer);
        //graph.DFS(startVer);
        graph.TopologicalSort();
        scan.close(); // Close the scanner at the end of the program
    }
}
