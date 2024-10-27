
import java.io.*;
import java.util.*;

class Graph {
    int vertice_no;
    int edge_no;
    LinkedList<LinkedList<Integer>> adjacencyList;

    Graph(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        vertice_no = scanner.nextInt();
        edge_no = scanner.nextInt();
        adjacencyList = new LinkedList<>();
        
        for (int i = 1; i <= vertice_no; i++) {  // Assuming vertices are 0-based
            adjacencyList.add(new LinkedList<>());
        }

        // Read edges and populate adjacency list
        for (int i = 1; i <= edge_no; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            addEdge(u, v);
        }
        scanner.close();
    }

    public void addEdge(int u, int v) {
        adjacencyList.get(u).add(v); // Assuming 1-based index for vertices
       // adjacencyList.get(v).add(u); // for undirected graph
    }
    
    public void addVertex(int n) { // add extra n vertices
        for (int i = 1; i <= n; i++) {
            adjacencyList.add(new LinkedList<>());
        }
        vertice_no += n;
    }


    public void displayGraph() {
        for (int i = 1; i <= vertice_no; i++) {  // Assuming vertices are 1-based
            System.out.print("Vertex " + i + ": ");
            for (Integer neighbor : adjacencyList.get(i)) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }

    public void BFS(int startVertex) {
        boolean[] visited = new boolean[vertice_no + 1]; // Track visited vertices
        int[] distances = new int[vertice_no + 1]; // Track distances from startVertex
        Arrays.fill(distances, -1); // Initialize distances to -1 (unreachable)

        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        distances[startVertex] = 0;
        queue.add(startVertex);

        System.out.println("BFS traversal starting from vertex " + startVertex + ":");

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");

            for (int neighbor : adjacencyList.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distances[neighbor] = distances[current] + 1;
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();

        // Display shortest path (distance) to all vertices
        System.out.println("Shortest paths from vertex " + startVertex + ":");
        for (int i = 1; i <= vertice_no; i++) {
            String distanceStr = (distances[i] == -1) ? "Unreachable" : String.valueOf(distances[i]);
            System.out.println("To vertex " + i + ": " + distanceStr);
        }
    }

    public void DFS(int startVertex) {
        boolean[] visited = new boolean[vertice_no];
        System.out.print("DFS Traversal: ");
        DFSUtil(startVertex, visited);
        System.out.println();
    }

    private void DFSUtil(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");
        for (Integer neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                DFSUtil(neighbor, visited);
            }
        }
    }

    public List<Integer> topologicalSort() {
        boolean[] visited = new boolean[vertice_no];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < vertice_no; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void topologicalSortUtil(int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true;

        for (Integer neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }
        stack.push(vertex);
    }
}

public class Graph_Algo {
    public static void main(String[] args) {
    
        String filename = "input.txt"; // Default input file name

        // If a filename is provided as an argument, use it
        if (args.length > 0) {
            filename = args[0];
        }

        try {
            Graph graph = new Graph(filename);

            System.out.println("Graph adjacency list:");
            graph.displayGraph();
            int startVertex=5;

            // Optionally, allow the user to specify the start vertex via command-line arguments
            // if (args.length > 1) {
            //     try {
            //         startVertex = Integer.parseInt(args[1]);
            //         if (startVertex < 1 || startVertex > graph.vertice_no) {
            //             System.out.println("Start vertex out of range. Using default vertex 1.");
            //             startVertex = 1;
            //         }
            //     } catch (NumberFormatException e) {
            //         System.out.println("Invalid start vertex provided. Using default vertex 1.");
            //         startVertex = 1;
            //     }
            // }

            System.out.println("\nPerforming BFS starting from vertex 5:");
            graph.BFS(startVertex);

            System.out.println("\nPerforming DFS starting from vertex 5:");
            graph.DFS(startVertex);

            System.out.println("\nPerforming Topological Sort:");
            List<Integer> topoOrder = graph.topologicalSort();
            System.out.println("Topological Sort order: " + topoOrder);
        } catch (IOException e) {
            System.out.println("Error reading the graph: " + e.getMessage());
        }
    }
}