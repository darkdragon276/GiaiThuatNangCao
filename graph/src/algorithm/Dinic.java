package algorithm;

import java.util.*;

public class Dinic extends Network {
    public Dinic(int NumOfVertices) {
        super(NumOfVertices);
    }
    public boolean bfs(int source, int sink, int[] levelArrays) {
        Arrays.fill(levelArrays, -1);
        levelArrays[source] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge uv : getListAdjency(u)) {
                if (uv != null && levelArrays[uv.getV()] == -1 && uv.getCapacity() > 0) {
                    levelArrays[uv.getV()] = levelArrays[u] + 1;
                    queue.add(uv.getV());
                }
            }
        }
        return levelArrays[sink] != -1;
    }

    public int dfs(int source, int sink, int[] levelArrays) {
        Stack<Integer> stack = new Stack<>();
        stack.push(source);

        int bottleNeck = Integer.MAX_VALUE;
        List<Integer> verticesStore = new ArrayList<>();
        verticesStore.add(source);
        outerLoop:
        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (Edge uv: getListAdjency(u)) {
                if (uv != null && levelArrays[uv.getV()] == levelArrays[u] + 1 && uv.getCapacity() > 0) {
                    stack.push(uv.getV());
                    if (uv.getV() == sink) {
                        verticesStore.add(uv.getV());
                        bottleNeck = Math.min(bottleNeck, uv.getCapacity());
                        break outerLoop;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(verticesStore.toArray()));
        for (int i = 0; i < verticesStore.size() - 1; i++) {
            addFlowToEdge(verticesStore.get(i), verticesStore.get(i + 1), bottleNeck);
        }
        return bottleNeck;
    }

    @Override
    public int getMaximumFlow(int source, int sink) {
        int[] levelArrays = new int[getNumOfVertices()];
        int maxFlow = 0;
        if (bfs(source, sink, levelArrays)) {
            System.out.println(Arrays.toString(levelArrays));
            int bottleNeck = 0;
            while ((bottleNeck = dfs(source, sink, levelArrays)) != 0) {
//                System.out.println(bottleNeck);
                maxFlow += bottleNeck;
            }
        }
        return maxFlow;
    }

    public static void main(String[] args) {
        int n = 11;
        int s = n - 1;
        int t = n - 2;
        Dinic dinic = new Dinic(12);
        dinic.buildGraph();
        System.out.println("The maximum possible flow is " + dinic.getMaximumFlow(s, t));
    }
}
