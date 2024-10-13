package com.rift.nguyen.service;

import java.util.*;

public class TopologicalSortDFS {
    private final Set<Integer> visited = new HashSet<>();
    private final Set<Integer> recStack = new HashSet<>();
    private final List<Integer> topoOrder = new ArrayList<>();
    
    public boolean topologysort(int[][] edges) {
        // Build adjacency list
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        for (int[] edge : edges) {
            graph.putIfAbsent(edge[0], new ArrayList<>());
            graph.get(edge[0]).add(edge[1]);
        }
        
        // Perform DFS on each unvisited node
        for (int node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (!dfs(node, graph)) {
                    return false;  // cycle detected, topological sort not possible
                }
            }
        }
        
        // Reverse the topoOrder to get the correct order
        Collections.reverse(topoOrder);
        System.out.println("Topological Sort Order: " + topoOrder);
        return true;
    }
    
    private boolean dfs(int node, Map<Integer, List<Integer>> graph) {
        // Add node to recursion stack
        if (recStack.contains(node)) {
            return false; // Cycle detected
        }
        if (visited.contains(node)) {
            return true;  // Already processed
        }
        
        // Mark the node as visited and add to recursion stack
        recStack.add(node);
        visited.add(node);
        
        // Process all neighbors
        if (graph.containsKey(node)) {
            for (int neighbor : graph.get(node)) {
                if (!dfs(neighbor, graph)) {
                    return false;  // Cycle detected
                }
            }
        }
        
        // Remove from recursion stack and add to topological order
        recStack.remove(node);
        topoOrder.add(node);
        
        return true;
    }
}

