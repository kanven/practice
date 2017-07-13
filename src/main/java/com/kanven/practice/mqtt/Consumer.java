package com.kanven.practice.mqtt;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;


public class Consumer {

	public static void main(String[] args) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost("ssl://10.1.93.177:8883");
		mqtt.setClientId("tt");
		mqtt.setSslContext(Producer.getSSLContext());
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		Topic[] topics = {new Topic("mqtt", QoS.EXACTLY_ONCE)};
		connection.subscribe(topics);
		Message message = connection.receive();
		System.out.println(new String(message.getPayload()));
		message.ack();
		connection.disconnect();
	}

}
