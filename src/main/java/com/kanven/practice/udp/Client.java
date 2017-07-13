package com.kanven.practice.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Client {

	public static void main(String[] args) throws IOException {
		String send = "Hello server";
		DatagramSocket client = new DatagramSocket(3000);
		DatagramPacket p = new DatagramPacket(send.getBytes(), send.length(),
				new InetSocketAddress("10.1.94.59", 9999));
		client.send(p);
		boolean f = true;
		while (f) {
			byte[] buf = new byte[1024];
			DatagramPacket receiver = new DatagramPacket(buf, 1024);
			client.receive(receiver);
			String msg = new String(receiver.getData(), 0, receiver.getLength());
			System.out.println(msg + " from " + receiver.getAddress() + ":" + receiver.getPort());
		}
		client.close();
	}

}
