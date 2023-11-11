package algorithm;

public class Edge {
    private final int u;
    private final int v;
    private final int maxCapacity;
    private final boolean isReverseEdge;
    private int flow;

    public Edge(int u, int v, int maxCapacity) {
        this.flow = 0;
        this.u = u;
        this.v = v;
        this.maxCapacity = maxCapacity;
        this.isReverseEdge = false;
    }


    public Edge(int u, int v, int maxCapacity, boolean isReverseEdge) {
        this.flow = 0;
        this.u = u;
        this.v = v;
        this.maxCapacity = maxCapacity;
        this.isReverseEdge = isReverseEdge;
    }

    public boolean isReverseEdge() {
        return isReverseEdge;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCapacity() {
        return isReverseEdge ? flow : maxCapacity - flow;
    }

    public void addCapacity(int additionFlow) {
        flow = isReverseEdge ? flow - additionFlow : flow + additionFlow;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d] = {%d, %d}", u, v, maxCapacity, flow);
    }
}
