package com.kanven.practice.hazelcast.collection.queue;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class Producer {

	public static void main(String[] args) throws InterruptedException {
		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		IQueue<Integer> queue = instance.getQueue("queue");
		for (int i = 0; i < 1000; i++) {
			queue.put(i);
		}
		queue.put(-1);
	}

}
