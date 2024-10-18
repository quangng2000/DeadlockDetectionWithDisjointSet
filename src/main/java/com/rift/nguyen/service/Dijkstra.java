package com.rift.nguyen.service;
import java.util.*;

public class Dijkstra {
	
	
	private Map<Integer, List<Edge>> adjacencyList;
    private int numNodes;

    public Dijkstra(int numNodes, int[][] edges) {
        this.numNodes = numNodes;
        this.adjacencyList = new HashMap<>();
        // Initialize adjacency list
        for (int i = 0; i < numNodes; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
        // Add edges to the graph
        for (int[] edge : edges) {
            int source = edge[0];
            int destination = edge[1];
            int weight = edge[2];
            adjacencyList.get(source).add(new Edge(destination, weight));
        }
    }

    public Map<Integer, Integer> dijkstraShortestPath(int source) {
        // Distance map to store shortest distances from source
        Map<Integer, Integer> distances = new HashMap<>();
        for (int i = 0; i < numNodes; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        // Priority queue for selecting the node with the smallest distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(NodeDistance::getDistance));
        pq.add(new NodeDistance(source, 0));

        // Main Dijkstra's algorithm loop
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            int currentNode = current.getNode();
            int currentDistance = current.getDistance();

            // Iterate through all neighbors
            for (Edge edge : adjacencyList.get(currentNode)) {
                int neighbor = edge.getTargetNode();
                int newDist = currentDistance + edge.getWeight();

                // If a shorter path is found
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    pq.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        return distances;  // Return shortest distances
    }

    // Inner class to represent an edge
    private static class Edge {
        private final int targetNode;
        private final int weight;

        public Edge(int targetNode, int weight) {
            this.targetNode = targetNode;
            this.weight = weight;
        }

        public int getTargetNode() {
            return targetNode;
        }

        public int getWeight() {
            return weight;
        }
    }

    // Inner class to represent a node and its distance in the priority queue
    private static class NodeDistance {
        private final int node;
        private final int distance;

        public NodeDistance(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        public int getNode() {
            return node;
        }

        public int getDistance() {
            return distance;
        }
    }

}
