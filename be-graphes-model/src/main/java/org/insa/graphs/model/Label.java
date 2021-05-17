package org.insa.graphs.model;

public class Label implements Comparable<Label> {
    private final Node node;
    private boolean marked;
    private double cost;
    private int fatherId;

    public Label(Node node, boolean marked, double cost, int fatherId) {
        this.node = node;
        this.marked = marked;
        this.cost = cost;
        this.fatherId = fatherId;
    }

    public Node getNode() {
        return this.node;
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

    public double getTotalCost() {
        return cost;
    }

    public int getFatherId() {
        return this.fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public double getHeuristic() {
        return 0;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), other.getTotalCost());
    }
}
