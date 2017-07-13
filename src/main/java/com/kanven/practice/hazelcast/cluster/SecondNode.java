package com.kanven.practice.hazelcast.cluster;

import java.util.Map;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class SecondNode {

	public static void main(String[] args) {
		Config config = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
		Map<Integer, String> custorms =  instance.getMap("custorms");
		custorms.put(1, "jiangyl");
		custorms.put(3, "lzy");
	}

}
