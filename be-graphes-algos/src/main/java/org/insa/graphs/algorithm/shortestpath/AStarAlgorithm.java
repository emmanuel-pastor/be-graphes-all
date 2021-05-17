package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;

import static org.insa.graphs.algorithm.AbstractInputData.Mode;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    Label[] initLabels(int nbNodes, ShortestPathData data) {
        LabelStar[] output = new LabelStar[nbNodes];
        Graph graph = data.getGraph();
        Node destination = data.getDestination();
        boolean isLengthMode = data.getMode() == Mode.LENGTH;
        for (int i = 0; i < nbNodes; i++) {
            output[i] = new LabelStar(graph.get(i), false, Double.POSITIVE_INFINITY, -1, destination, isLengthMode);
        }
        return output;
    }
}
