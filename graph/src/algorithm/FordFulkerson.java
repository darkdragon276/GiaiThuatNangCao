package algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class FordFulkerson extends Network {
    public FordFulkerson(int NumOfVertices) {
        super(NumOfVertices);
    }

    public boolean dfs(int source, int sink, int[] retParentArrays) {
        boolean[] visited = new boolean[getNumOfVertices()];
        Arrays.fill(visited, false);

        Stack<Integer> stack = new Stack<>();
        visited[source] = true;
        stack.push(source);
        retParentArrays[source] = -1;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (Edge uv : getListAdjency(u)) {
                if (uv != null && !visited[uv.getV()] && uv.getCapacity() > 0) {
                    if (uv.getV() == sink) {
                        retParentArrays[uv.getV()] = u;
                        return true;
                    }
                    stack.push(uv.getV());
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
        while (dfs(source, sink, parent)) {
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
        FordFulkerson fordFulkerson = new FordFulkerson(100);
        fordFulkerson.buildGraph();
        System.out.println(fordFulkerson.toString());
        System.out.println("fordFulkerson The maximum possible flow is " + fordFulkerson.getMaximumFlow(0, 13));
    }
}
