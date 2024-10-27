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
        adjacencyList.get(v).add(u); //undirected
    }
}


class BFSandDFS{
    Graph g;
    String[] color;
    int[] prev, distance, discovery, finish;
    int inf = 1000000, time=0;
    List<List<Integer>> connectedComponents = new ArrayList<>();

    BFSandDFS(Graph graph) {
        this.g= graph;
        this.color = new String[this.g.verticeNo];
        this.prev = new int[this.g.verticeNo];
        this.discovery = new int[this.g.verticeNo];
        this.finish = new int[this.g.verticeNo];
    }

    public void DFS(){
        this.time = 0;
        this.connectedComponents.clear();
        for (int i = 0; i < this.g.verticeNo; i++) {  
            color[i] = "WHITE";
            prev[i] = -2;
            finish[i] = 0;
            discovery[i] = 0;
        }
        
        for (int i = 0; i < this.g.verticeNo; i++) {
            if (color[i].equals("WHITE")) {
                List<Integer> component = new ArrayList<>();
                DFS_Visit(i, component);   
                connectedComponents.add(component);
            }
        }
    }

    public void DFS_Visit(int u, List<Integer> component){
        color[u] = "GRAY";
        this.time = this.time + 1;  
        discovery[u] = this.time;
        component.add(u);
        for (int v : this.g.adjacencyList.get(u)) {
            if (color[v].equals("WHITE")) {
                prev[v] = u;
                DFS_Visit(v, component);
            }
        }      
        color[u] = "BLACK";
        this.time = this.time + 1;
        finish[u] = this.time;
    }

    public void printConnectedComponents(int caseNumber) {
        System.out.println("Case #" + caseNumber + ":");
        for (List<Integer> component : connectedComponents) {
            Collections.sort(component);  // Ensure ascending order of vertices
            for (int vertex : component) {
                char vertexChar = (char) (vertex + 'a');
                System.out.print(vertexChar + ",");
            }
            System.out.println();
        }
        System.out.println(connectedComponents.size() + " connected components\n");
    }

    public void PrintAllValuesForDFS(){// Output the result for color, prev, and distance
        System.out.println("\nVertex\tStart\tFinish");
        for (int i = 0; i < this.g.verticeNo; i++) {
            System.out.println(i + "\t" + discovery[i] + "\t" + finish[i]);
        }
    }
    public void PrintAllValuesForBFS() { // Output the result for color, prev, and distance
        System.out.println("\nBFS Results:");
        System.out.println("Vertex\tColor\tPredecessor\tDistance");
        for (int i = 0; i < this.g.verticeNo; i++) {
            String predecessor = (prev[i] == -1) ? "None" : String.valueOf(prev[i] + 1);
            String distanceStr = (distance[i] == inf) ? "INF" : String.valueOf(distance[i]);
            System.out.println((i + 1) + "\t" + color[i] + "\t" + predecessor + "\t\t" + distanceStr);
        }
    }
    
    
}


public class connectedcomponents {
    public static void main(String[] args) {
        String path = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String test = br.readLine();
            int i = Integer.parseInt(test);
            for (int j = 0; j < i; j++) {
            
            Graph graph = new Graph();
            String[] ve = br.readLine().trim().split("\\s+");
            
            int verNo = Integer.parseInt(ve[0]);
            int edgeNo = Integer.parseInt(ve[1]);

            graph.initializeGraph(verNo, edgeNo);

            for (int k = 0; k < edgeNo; k++) {
                String line = br.readLine();
                if (line == null) {
                    System.err.println("Insufficient edge data in the input file.");
                    break;
                }
                String[] edge = line.trim().split("\\s+");
                if (edge.length < 2) {
                    System.err.println("Invalid edge format: " + line);
                    continue;
                }
                int u = edge[0].toLowerCase().charAt(0) - 'a';
                int v = edge[1].toLowerCase().charAt(0) - 'a';

                graph.addEdge(u, v);
            }
            BFSandDFS traversal = new BFSandDFS(graph);
        
            //System.out.println("\nDFS Traversal:");
            traversal.DFS();
            traversal.printConnectedComponents(i);
        }
        } catch (IOException ex) {
            System.err.println("Error Reading File: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.err.println("Invalid number format in the input file.");
        }

    }

    
}
