package algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class EdmondsKarp extends Network {
    public EdmondsKarp(int NumOfVertices) {
        super(NumOfVertices);
    }

    public boolean bfs(int source, int sink, int[] retParentArrays) {
        boolean[] visited = new boolean[getNumOfVertices()];
        Arrays.fill(visited, false);

        Queue<Integer> queue = new LinkedList<>();
        visited[source] = true;
        queue.add(source);
        retParentArrays[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge uv : getListAdjency(u)) {
                if (uv != null && !visited[uv.getV()] && uv.getCapacity() > 0) {
                    if (uv.getV() == sink) {
                        retParentArrays[uv.getV()] = u;
                        return true;
                    }
                    queue.add(uv.getV());
                    retParentArrays[uv.getV()] = u;
                    visited[uv.getV()] = true;
                }
            }
        }

        // We didn't reach sink in BFS starting from source,
        // so return false
        return false;
    }

    @Override
    public int getMaximumFlow(int source, int sink) {
        int[] parent = new int[getNumOfVertices()];
        int maxFlow = 0;
        while (bfs(source, sink, parent)) {
            int bottleNeck = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                bottleNeck = Math.min(bottleNeck, getEdge(parent[v], v).getCapacity());
            }

            for (int v = sink; v != source; v = parent[v]) {
                addFlowToEdge(parent[v], v, bottleNeck);
            }
            maxFlow += bottleNeck;
        }
        return maxFlow;
    }

    public static void main(String[] args) {
        EdmondsKarp edmondsKarp = new EdmondsKarp(100);
        edmondsKarp.buildGraph();
        System.out.println(edmondsKarp.toString());
        System.out.println("The maximum possible flow is " + edmondsKarp.getMaximumFlow(0, 13));
    }
}
