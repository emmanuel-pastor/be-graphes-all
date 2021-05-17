package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.algorithm.utils.EmptyPriorityQueueException;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    Label[] initLabels(int nbNodes, ShortestPathData data) {
        Label[] output = new Label[nbNodes];
        Graph graph = data.getGraph();
        for (int i = 0; i < nbNodes; i++) {
            output[i] = new Label(graph.get(i), false, Double.POSITIVE_INFINITY, -1);
        }
        return output;
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of labels.
        Label[] labels = initLabels(nbNodes, data);
        labels[data.getOrigin().getId()].setCost(0);
        //Initialize the heap
        BinaryHeap<Label> heap = new BinaryHeap<>();
        heap.insert(labels[data.getOrigin().getId()]);

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        while (!labels[data.getDestination().getId()].isMarked()) {
            Label currentNodeLabel;
            try {
                currentNodeLabel = heap.findMin();
            } catch (EmptyPriorityQueueException e) {
                // Means that no new node was marked after the previous one
                // And the previous node was the only one visited but not marked
                // Means we reached and mark all nodes that we can visit
                break;
            }

            // Mark the node
            try {
                heap.remove(currentNodeLabel);
            } catch (ElementNotFoundException ignored) { }
            labels[currentNodeLabel.getNode().getId()].setMarked(true);

            for (Arc successor : graph.get(currentNodeLabel.getNode().getId()).getSuccessors()) {
                // If the road is not allowed for us: skip this road
                if (!data.isAllowed(successor)) continue;

                int currentNodeId = successor.getOrigin().getId();
                int nextNodeId = successor.getDestination().getId();
                if (!labels[nextNodeId].isMarked()) {
                    double oldCost = labels[nextNodeId].getTotalCost();
                    double w = data.getCost(successor) + labels[nextNodeId].getHeuristic();
                    double newCost = labels[currentNodeId].getCost() + w;

                    if (Double.isInfinite(oldCost) && Double.isFinite(newCost)) {
                        notifyNodeReached(successor.getDestination());
                    }

                    // Check if new cost is lower, if so update...
                    if (newCost < oldCost) {
                        labels[nextNodeId].setCost(labels[currentNodeId].getCost() + data.getCost(successor));
                        labels[nextNodeId].setFatherId(currentNodeLabel.getNode().getId());

                        try {
                            // Update the node in the heap
                            heap.remove(labels[nextNodeId]);
                            heap.insert(labels[nextNodeId]);
                        } catch (ElementNotFoundException e) {
                            // If the node is not in the heap, insert it
                            heap.insert(labels[nextNodeId]);
                        }
                    }
                }
            }
        }

        ShortestPathSolution solution;

        if (!labels[data.getDestination().getId()].isMarked()) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            ArrayList<Node> pathNodes = new ArrayList<>();
            pathNodes.add(data.getDestination());
            Node node = data.getDestination();
            while (!node.equals(data.getOrigin())) {
                Node fatherNode = graph.getNodes().get(labels[node.getId()].getFatherId());
                pathNodes.add(fatherNode);
                node = fatherNode;
            }
            Collections.reverse(pathNodes);

            // Create the final solution.
            Path solutionPath;
            if (data.getMode().equals(AbstractInputData.Mode.LENGTH)) {
                solutionPath = Path.createShortestPathFromNodes(graph, pathNodes);
            } else {
                solutionPath = Path.createFastestPathFromNodes(graph, pathNodes);
            }

            solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, solutionPath);
        }

        return solution;
    }

}
