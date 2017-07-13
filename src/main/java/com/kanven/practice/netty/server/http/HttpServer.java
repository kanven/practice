package com.kanven.practice.netty.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer {

	private static final int DEFAULT_PORT = 8080;

	private static final int DEFAULT_MAX_CONTENT_LENGTH = 1 << 16;

	private int port = DEFAULT_PORT;

	private String host;

	public HttpServer(String host) {
		this(host, DEFAULT_PORT);
	}

	public HttpServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup works = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, works).channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpResponseEncoder());
						ch.pipeline().addLast(new HttpRequestDecoder());
						ch.pipeline().addLast(new HttpObjectAggregator(DEFAULT_MAX_CONTENT_LENGTH));
						ch.pipeline().addLast(new HttpServerChannelHandler(host));
					}
				});
		ChannelFuture future = bootstrap.bind(port).sync();
		future.channel().closeFuture();
	}

	public static void main(String[] args) throws InterruptedException {
		new HttpServer("/hdbs").start();
	}

}
