package com.kanven.practice.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class HelloWordCommand extends HystrixCommand<String> {

	private String name;

	protected HelloWordCommand(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("hello-example"));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		throw new RuntimeException("");
	}
	
	

	@Override
	protected String getFallback() {
		return "error";
	}

	public static void main(String[] args) {
		System.out.println(new HelloWordCommand("jyl").execute());
	}

}
