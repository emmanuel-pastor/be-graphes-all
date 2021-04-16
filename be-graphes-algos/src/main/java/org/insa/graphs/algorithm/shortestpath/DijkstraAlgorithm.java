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

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of labels.
        Label[] labels = new Label[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new Label(i, false, Double.POSITIVE_INFINITY, -1);
        }
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
            labels[currentNodeLabel.getNodeId()].setMarked(true);

            for (Arc successor : graph.get(currentNodeLabel.getNodeId()).getSuccessors()) {
                if(!data.isAllowed(successor)) continue;

                int nextNodeId = successor.getDestination().getId();
                if (!labels[nextNodeId].isMarked()) {
                    double w = data.getCost(successor);
                    double oldDistance = labels[nextNodeId].getCost();
                    double newDistance = labels[currentNodeLabel.getNodeId()].getCost() + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(successor.getDestination());
                    }

                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        labels[nextNodeId].setCost(newDistance);
                        labels[nextNodeId].setFatherId(currentNodeLabel.getNodeId());

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
