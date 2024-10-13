package com.rift.nguyen.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class MyService {
	
	ThreadDumpParser TDP = new ThreadDumpParser();
	
	
	
	public boolean DetectDreadLockUF(List<String> threadDump) {
		
		Map<String, Set<String>> graph = TDP.parseThreadDump(threadDump);
		
		
		int[][] edges = TDP.convertGraphToEdges(graph);
		
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
