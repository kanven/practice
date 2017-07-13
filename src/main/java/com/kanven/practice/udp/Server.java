package com.kanven.practice.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

	private static final int DEFAULT_PORT = 9999;

	public static void main(String[] args) throws IOException {
		DatagramSocket server = new DatagramSocket(DEFAULT_PORT);
		byte[] buf = new byte[1024];
		DatagramPacket p = new DatagramPacket(buf, 1024);
		boolean f = true;
		String msg = "Hello client";
		while (f) {
			server.receive(p);
			String content = new String(p.getData(), 0, p.getLength());
			System.out.println(content + " from " + p.getAddress() + ":" + p.getPort());
			DatagramPacket send = new DatagramPacket(msg.getBytes(), msg.length(), p.getAddress(), p.getPort());
			server.send(send);
			p.setLength(1024);
		}
		server.close();
	}

}
