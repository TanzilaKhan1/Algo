
import java.io.*;
import java.util.*;

class Graph {
    int verticeNo;
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

    public void addEdge(int u, int v, boolean isLeftChild) {
        if (u < 0 || u >= this.verticeNo || v < 0 || v >= this.verticeNo) {
            System.err.println("Invalid edge: " + u + " -> " + v);
            return;
        }
        if (isLeftChild) {
            adjacencyList.get(u).addFirst(v);
        } else {
            adjacencyList.get(u).addLast(v);
        }
    }
}

class BFSandDFS {
    Graph g;
    String[] color;
    List<Integer> discoveryOrder = new ArrayList<>();  // To store the order of discovery

    BFSandDFS(Graph graph) {
        this.g = graph;
        this.color = new String[this.g.verticeNo];
    }

    public void startBFS(int startVer) {
        discoveryOrder.clear();
        for (int i = 0; i < this.g.verticeNo; i++) {
            color[i] = "WHITE";
        }
        color[startVer] = "GRAY";

        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVer);
        discoveryOrder.add(startVer);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : this.g.adjacencyList.get(u)) {
                if (color[v].equals("WHITE")) {
                    color[v] = "GRAY";
                    queue.add(v);
                    discoveryOrder.add(v);
                }
            }
            color[u] = "BLACK";
        }
    }
}

public class LevelOrderTreeTraversal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int C = sc.nextInt();
        sc.nextLine(); // Consume the newline
        for (int testCase = 1; testCase <= C; testCase++) {
            int N = sc.nextInt();
            sc.nextLine(); // Consume the newline
            int[] nums = new int[N];
            String[] numStrs = sc.nextLine().trim().split("\\s+");
            for (int i = 0; i < N; i++) {
                nums[i] = Integer.parseInt(numStrs[i]);
            }

            int[] leftChild = new int[N];
            int[] rightChild = new int[N];
            Arrays.fill(leftChild, -1);
            Arrays.fill(rightChild, -1);

            Graph g = new Graph();
            g.initializeGraph(N);

            // Build the BST
            for (int i = 1; i < N; i++) {
                int currentIndex = 0;
                while (true) {
                    if (nums[i] < nums[currentIndex]) {
                        if (leftChild[currentIndex] == -1) {
                            leftChild[currentIndex] = i;
                            g.addEdge(currentIndex, i, true); // Add left child first
                            break;
                        } else {
                            currentIndex = leftChild[currentIndex];
                        }
                    } else {
                        if (rightChild[currentIndex] == -1) {
                            rightChild[currentIndex] = i;
                            g.addEdge(currentIndex, i, false); // Add right child last
                            break;
                        } else {
                            currentIndex = rightChild[currentIndex];
                        }
                    }
                }
            }

            // Perform BFS
            BFSandDFS bfs = new BFSandDFS(g);
            bfs.startBFS(0);

            // Output
            System.out.println("Case " + testCase + ":");
            List<Integer> discoveryOrder = bfs.discoveryOrder;
            for (int i = 0; i < discoveryOrder.size(); i++) {
                int index = discoveryOrder.get(i);
                System.out.print(nums[index]);
                if (i != discoveryOrder.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println("\n");
        }
        sc.close();
    }
}
