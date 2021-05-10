package org.insa.graphs.algorithm.shortestpath;

public class BellmanFordAlgorithmTest extends ShortestPathAlgorithmTest{
    @Override
    public ShortestPathAlgorithm createShortestPathAlgorithm(ShortestPathData data) {
        return new BellmanFordAlgorithm(data);
    }
}
