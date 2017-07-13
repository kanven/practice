package com.kanven.practice.netty.client.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import com.kanven.practice.netty.utils.OsUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;

public class HttpClient {

	private String host;

	private int port;

	private Channel channel;

	public HttpClient(String host, int port) throws InterruptedException {
		this.host = host;
		this.port = port;
		connection();
	}

	private void connection() throws InterruptedException {
		EventLoopGroup work = new NioEventLoopGroup();
		Bootstrap boot = new Bootstrap();
		boot.group(work);
		boot.channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpResponseDecoder());
						ch.pipeline().addLast(new HttpRequestEncoder());
						ch.pipeline().addLast(new HttpClientChannelHandler());
					}
				});
		ChannelFuture future = boot.connect(host, port).sync();
		this.channel = future.channel();
	}

	public void request(URI uri, byte[] data) throws URISyntaxException, UnsupportedEncodingException {
		// 请求行设置
		DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
				uri.toASCIIString());
		// 请求正文设置
		ByteBuf content = request.content();
		content.writeBytes(data);
		// 请求头设置
		HttpHeaders headers = request.headers();
		headers.set(Names.HOST, uri.getHost());
		headers.set(Names.USER_AGENT, OsUtil.getOsName());
		headers.set(Names.CONNECTION, Values.KEEP_ALIVE);
		headers.set(Names.CONTENT_LENGTH, content.readableBytes());
		// 发送请求
		channel.write(request);
		channel.flush();
	}

	public void close() throws InterruptedException {
		if (channel != null) {
			channel.close();
		}
	}

	public static void main(String[] args)
			throws UnsupportedEncodingException, InterruptedException, URISyntaxException {
		HttpClient client = new HttpClient("127.0.0.1", 8080);
		client.request(new URI("http://127.0.0.1:8080"), "Are you ok?".getBytes());
		client.close();
	}

}
