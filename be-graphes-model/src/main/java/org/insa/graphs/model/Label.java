package org.insa.graphs.model;

public class Label implements Comparable<Label> {
    private final int nodeId;
    private boolean marked;
    private double cost;
    private int fatherId;

    public Label(int nodeId, boolean marked, double cost, int fatherId) {
        this.nodeId = nodeId;
        this.marked = marked;
        this.cost = cost;
        this.fatherId = fatherId;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public boolean isMarked() {
        return marked;
    }
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getFatherId() {
        return this.fatherId;
    }
    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.cost, other.getCost());
    }
}
