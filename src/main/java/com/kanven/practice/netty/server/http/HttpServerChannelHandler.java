package com.kanven.practice.netty.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServerChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private String host;

	public HttpServerChannelHandler(String host) {
		this.host = host;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (request.getDecoderResult().isFailure()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		
		System.out.println(paraseContent(request));
	}
	
	private String paraseContent(FullHttpRequest request){
		ByteBuf buf = request.content();
		String content = buf.toString(CharsetUtil.UTF_8);
		return content;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure:" + status.toString(), CharsetUtil.UTF_8));
		response.headers().set("content-type", "text/plain");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
