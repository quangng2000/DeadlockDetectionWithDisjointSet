package com.rift.nguyen.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class MyService {
	
	public boolean DetectDreadLockUF(List<String> threadDump) {
		
		Map<String, Set<String>> graph = ThreadDumpParser.parseThreadDump(threadDump);
		
		
		int[][] edges = ThreadDumpParser.convertGraphToEdges(graph);
		
		UnionFind uf = new UnionFind(edges.length);
		
		
		for (int[] edge : edges) {
            int thread1 = edge[0];
            int thread2 = edge[1];

            // If the two threads are already connected, there's a cycle
            if (!uf.union(thread1, thread2)) {
                return true;  // Cycle detected
            }
        }

        // No cycle found
        return false;
		
		
		
	}
	
	

}
