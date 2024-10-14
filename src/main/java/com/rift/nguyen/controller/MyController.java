package com.rift.nguyen.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rift.nguyen.service.MyService;

@RestController
public class MyController {

    @Autowired
    private MyService myService; // Ensure proper annotation for field injection

    @GetMapping("/deadlockUF")
    public String cycleController() {
        // Assuming myService has a method that checks for cycles
    	
    	List<String> threadDump = Arrays.asList(
			    "\"Thread-1\" #10 prio=5 os_prio=0 tid=0x00007f8e6000a800 nid=0x109 waiting for monitor entry [0x00007f8e77c4f000]",
			    "   java.lang.Thread.State: BLOCKED (on object monitor)",
			    "    - waiting to lock <0x000000078ae03c48> (a java.lang.Object) owned by \"Thread-2\"",
			    "    at com.example.MyClass.methodA(MyClass.java:10)",
			    "    - waiting for a condition on object <0x000000078ae03c48>",
			    "    - locked by <0x000000078ae03c48>",
			    
			    "\"Thread-2\" #11 prio=5 os_prio=0 tid=0x00007f8e6000a900 nid=0x10a waiting for monitor entry [0x00007f8e77c4e000]",
			    "   java.lang.Thread.State: BLOCKED (on object monitor)",
			    "    - waiting to lock <0x000000078ae03c50> (a java.lang.Object) owned by \"Thread-3\"",
			    "    at com.example.MyClass.methodB(MyClass.java:20)",
			    "    - waiting for a condition on object <0x000000078ae03c50>",
			    "    - locked by <0x000000078ae03c50>",
			    
			    "\"Thread-3\" #12 prio=5 os_prio=0 tid=0x00007f8e6000aa00 nid=0x10b waiting for monitor entry [0x00007f8e77c4e100]",
			    "   java.lang.Thread.State: BLOCKED (on object monitor)",
			    "    - waiting to lock <0x000000078ae03c58> (a java.lang.Object) owned by \"Thread-1\"",
			    "    at com.example.MyClass.methodC(MyClass.java:30)",
			    "    - waiting for a condition on object <0x000000078ae03c58>",
			    "    - locked by <0x000000078ae03c58>"
			);
    	
    	if(!myService.DetectDreadLockUF(threadDump)) {
    		return String.join(",\n", threadDump) + "  HAD DEADLOCK";
    	} 
    	
    	return String.join(",\n", threadDump) + "  HAD NO DEADLOCK";
    	
        
    }
}
