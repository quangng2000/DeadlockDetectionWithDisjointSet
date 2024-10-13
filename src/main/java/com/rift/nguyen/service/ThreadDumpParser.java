package com.rift.nguyen.service;
import java.util.*;
import java.util.regex.*;

public class ThreadDumpParser {

    private static final Pattern THREAD_PATTERN = Pattern.compile("\"([^\"]+)\".*tid=0x([a-fA-F0-9]+).*");
    private static final Pattern LOCK_HELD_PATTERN = Pattern.compile("- locked <(0x[a-fA-F0-9]+)>.*");
    private static final Pattern WAITING_FOR_LOCK_PATTERN = Pattern.compile("- waiting to lock <(0x[a-fA-F0-9]+)>.*owned by \"([^\"]+)\"");

    public Map<String, Set<String>> parseThreadDump(List<String> threadDump) {
        Map<String, String> threadHoldingLock = new HashMap<>();  // Maps lock -> thread holding it
        Map<String, Set<String>> waitingGraph = new HashMap<>();  // Graph of thread dependencies
        String currentThread = null;
        String currentLock = null;

        for (String line : threadDump) {
            Matcher threadMatcher = THREAD_PATTERN.matcher(line);
            if (threadMatcher.find()) {
                currentThread = threadMatcher.group(1);  // Get current thread
                waitingGraph.putIfAbsent(currentThread, new HashSet<>());
                continue;
            }

            Matcher lockHeldMatcher = LOCK_HELD_PATTERN.matcher(line);
            if (lockHeldMatcher.find()) {
                currentLock = lockHeldMatcher.group(1);  // Lock ID
                threadHoldingLock.put(currentLock, currentThread);  // Thread owns the lock
            }

            Matcher waitingMatcher = WAITING_FOR_LOCK_PATTERN.matcher(line);
            if (waitingMatcher.find()) {
                String lockId = waitingMatcher.group(1);  // Lock the thread is waiting on
                String ownerThread = waitingMatcher.group(2);  // Thread holding the lock
                if (currentThread != null) {
                    waitingGraph.get(currentThread).add(ownerThread);  // Current thread waits on owner thread
                }
            }
        }

        return waitingGraph;
    }
    
    public int[][] convertGraphToEdges(Map<String, Set<String>> graph) {
        // Step 1: Assign index to each thread
        Map<String, Integer> threadIndexMap = new HashMap<>();
        int index = 0;
        for (String thread : graph.keySet()) {
            threadIndexMap.put(thread, index++);
        }

        // Step 2: Create a list of edges
        List<int[]> edges = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            String thread = entry.getKey();
            Set<String> waitingThreads = entry.getValue();
            int threadIndex = threadIndexMap.get(thread);

            for (String waitingOn : waitingThreads) {
                if (threadIndexMap.containsKey(waitingOn)) {
                    int waitingOnIndex = threadIndexMap.get(waitingOn);
                    edges.add(new int[]{threadIndex, waitingOnIndex});  // Thread is waiting on another thread
                }
            }
        }

        // Step 3: Convert the list of edges to int[][]
        return edges.toArray(new int[0][]);
    }
    
    
}
