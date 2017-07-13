package com.kanven.practice.hazelcast.collection.queue;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class Consumer {

	public static void main(String[] args) {
		HazelcastInstance instance = Hazelcast.newHazelcastInstance();
		IQueue<Integer> queue = instance.getQueue("queue");
		System.out.println(queue.size());
	}

}
