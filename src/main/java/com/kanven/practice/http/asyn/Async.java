package com.kanven.practice.http.asyn;

import java.io.IOException;
import java.nio.CharBuffer;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;

public class Async {
	
	private final static CloseableHttpAsyncClient client  = HttpAsyncClients.createDefault();;

	public static void main(String[] args) {
		client.start();
		client.execute(HttpAsyncMethods.createGet("https://www.baidu.com"),
				new AsyncCharConsumer<Boolean>() {

					@Override
					protected void onCharReceived(CharBuffer buf, IOControl ioctrl) throws IOException {
						// TODO Auto-generated method stub

					}

					@Override
					protected void onResponseReceived(HttpResponse response) throws HttpException, IOException {
						System.out.println(response.getStatusLine());
						client.close();
					}

					@Override
					protected Boolean buildResult(HttpContext context) throws Exception {
						// TODO Auto-generated method stub
						return null;
					}
				}, null);
		System.out.println("=====");
	}
	
}
