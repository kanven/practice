package com.kanven.practice.brave.rpc;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

public class BraveRpcSender {

	public static void main(String[] args) {
		String url = "http://localhost:9411/api/v2/spans";
		OkHttpSender sender = OkHttpSender.create(url);
		AsyncReporter<zipkin2.Span> reporter = AsyncReporter.create(sender);
		Tracing tracing = Tracing.newBuilder().localServiceName("practice_grp").spanReporter(reporter).build();
		GrpcTracing grpcTracing = GrpcTracing.create(tracing);
	}

}
