package com.kanven.practice.brave.http;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

public class BraveHttpSender {

	public static void main(String[] args) {
		String url = "http://localhost:9411/api/v2/spans";
		OkHttpSender sender = OkHttpSender.create(url);
		AsyncReporter<zipkin2.Span> reporter = AsyncReporter.create(sender);
		Tracing tracing = Tracing.newBuilder().localServiceName("practice").spanReporter(reporter).build();
		Tracer tracer = tracing.tracer();
		Span span = tracer.newTrace();
		span.name("1111");
		span.tag("time", "99099");
		span.start();
		span.finish();
		reporter.close();
		sender.close();
	}

}
