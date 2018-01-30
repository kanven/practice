package com.kanven.practice.http.asyn;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.Future;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AsynHttp {

	private CloseableHttpAsyncClient client;

	@Before
	public void befor() {
		client = HttpAsyncClients.createDefault();
		client.start();
	}

	@Test
	public void testAysn() throws Exception {
		HttpGet get = new HttpGet("https://www.baidu.com");
		Future<HttpResponse> future = client.execute(get, null);
		HttpResponse response = future.get();
		System.out.println(response.getStatusLine() + ";" + response.getEntity());
	}

	@Test
	public void testConsumer() throws Exception {
		Future<Boolean> future = client.execute(HttpAsyncMethods.createGet("https://www.baidu.com"),
				new AsyncCharConsumer<Boolean>() {

					@Override
					protected void onCharReceived(CharBuffer buf, IOControl ioctrl) throws IOException {
						// TODO Auto-generated method stub

					}

					@Override
					protected void onResponseReceived(HttpResponse response) throws HttpException, IOException {
						System.out.println(response.getStatusLine());
					}

					@Override
					protected Boolean buildResult(HttpContext context) throws Exception {
						// TODO Auto-generated method stub
						return null;
					}
				}, null);
		System.out.println("=====");
		Boolean result = future.get();
		if(result != null && result) {
			System.out.println("====sucess");
		} else {
			System.out.println("====error");
		}
	}

	@After
	public void close() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
