package com.kanven.practice.hazelcast.client;

import java.util.Map;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class Client {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		config.addAddress("192.168.23.5:5705");
		HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
		// Map<Integer, String> custorms = client.getMap("custorms");
		IQueue<Integer> queue = client.getQueue("queue");
		// custorms.put(4, "jcx");
		System.out.println(queue.size());
	}

}
