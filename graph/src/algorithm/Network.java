package algorithm;

import org.jgrapht.generate.SimpleWeightedGraphMatrixGenerator;
import org.jgrapht.graph.*;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import java.util.Arrays;

public abstract class Network {
    private final int numOfVertices;
    private final Edge[][] graph;

    public Network(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.graph = new Edge[numOfVertices][numOfVertices];
    }

    public int getNumOfVertices() {
        return numOfVertices;
    }

    public Edge getEdge(int u, int v) {
        return graph[u][v];
    }

    public Edge[] getListAdjency(int u) {
        return graph[u].clone();
    }

    public void addEdge(int u, int v, int maxCapacity) {
        graph[u][v] = new Edge(u, v, maxCapacity);
        // add reverse edge
        graph[v][u] = new Edge(u, v, maxCapacity, true);
    }

    public void addFlowToEdge(int u, int v, int additionFlow) {
        graph[u][v].addCapacity(additionFlow);
        graph[v][u].addCapacity(additionFlow);
    }

    public String toString() {
        return Arrays.deepToString(graph);
    }

    public void buildGraph() {
        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (int i = 0; i < 100; i++) {
            graph.addVertex(i);
        }

        // Thêm 200 cạnh vào đồ thị với trọng số ngẫu nhiên
        for (int i = 0; i < 1000; i++) {
            int source = (int) (Math.random() * 99);
            int target = (int) (Math.random() * 99);
            int weight = (int) Math.round(Math.random() * 1000.0);

            if (source != target && !graph.containsEdge(source, target) && !graph.containsEdge(target, source)) {
                DefaultWeightedEdge edge = graph.addEdge(source, target);
                graph.setEdgeWeight(edge, weight);
                addEdge(source, target, weight);
            }
        }
    }

    public abstract int getMaximumFlow(int source, int sink);
}
