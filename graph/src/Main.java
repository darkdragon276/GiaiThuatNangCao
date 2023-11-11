import algorithm.EdmondsKarp;
import algorithm.FordFulkerson;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Main {

	public final static EdmondsKarp edmondsKarp = new EdmondsKarp(100);
	public final static FordFulkerson fordFulkerson = new FordFulkerson(100);
	public static void buildGraph() {
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
				edmondsKarp.addEdge(source, target, weight);
				fordFulkerson.addEdge(source, target, weight);
			}
		}
	}

	public static void main(String[] args) {
		Main.buildGraph();
		long startTime = System.nanoTime();
		int maxFlow = edmondsKarp.getMaximumFlow(0, 13);
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("edmondsKarp: maximum possible flow is " + maxFlow);
		System.out.println("total time for edmondsKarp: " + totalTime/1000000.0 + "ms");

		startTime = System.nanoTime();
		maxFlow = fordFulkerson.getMaximumFlow(0, 13);
		endTime   = System.nanoTime();
		totalTime = endTime - startTime;
		System.out.println("fordFulkerson: maximum possible flow is " + maxFlow);
		System.out.println("total time for fordFulkerson: " + totalTime/1000000.0 + "ms");
	}
}
