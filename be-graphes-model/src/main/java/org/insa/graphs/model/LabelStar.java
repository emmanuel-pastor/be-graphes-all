package org.insa.graphs.model;

public class LabelStar extends Label {
    private final double heuristic;

    public LabelStar(Node node, boolean marked, double cost, int fatherId, Node destination, boolean isLengthMode) {
        super(node, marked, cost, fatherId);

        if (isLengthMode) {
            this.heuristic = node.getPoint().distanceTo(destination.getPoint());
        } else {
            this.heuristic = node.getPoint().distanceTo(destination.getPoint()) / 25d; // Speed in m/s
        }
    }

    @Override
    public double getHeuristic() {
        return heuristic;
    }

    @Override
    public double getTotalCost() {
        return heuristic + getCost();
    }
}
