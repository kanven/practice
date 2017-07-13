package com.kanven.practice.hazelcast.topic;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class FirstHazelcastMessage implements MessageListener<String> {

	public static void main(String[] args) {
		Config config = new ClasspathXmlConfig("hazelcast.xml");
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
		ITopic<String> topic = instance.getTopic("mqtt");
		topic.addMessageListener(new FirstHazelcastMessage());
		topic.publish("I'am the first one!");
	}

	public void onMessage(Message<String> message) {
		System.out.println(this.getClass().getSimpleName() + ";message:" + message.getMessageObject());
	}

}
