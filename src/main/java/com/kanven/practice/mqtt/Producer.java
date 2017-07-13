package com.kanven.practice.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class Producer {
	
	private static final String password = "nykj,91160";

	public static KeyStore getKeyStore()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore store = KeyStore.getInstance("JKS");
		ClassLoader cl = Producer.class.getClassLoader();
		InputStream in = cl.getResourceAsStream("server.keystore");
		store.load(in, password.toCharArray());
		in.close();
		return store;
	}

	public static SSLContext getSSLContext()
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
		KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore store = getKeyStore();
		factory.init(store, password.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(store);
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(factory.getKeyManagers(), tmf.getTrustManagers(), null);
		return context;
	}

	public static void main(String[] args) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost("ssl://10.1.93.177:8883");
		mqtt.setSslContext(getSSLContext());
		mqtt.setCleanSession(false);
		mqtt.setClientId("jj");
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();
		connection.publish("notify","hello".getBytes(),QoS.EXACTLY_ONCE,false);
		connection.disconnect();
	}
	
}
