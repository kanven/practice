package com.kanven.practice.rxjava;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Flat {

	public static void main(String[] args) {
		Observable.from(Arrays.asList("http://www.baidu.com/", "http://www.google.com/", "https://www.bing.com/"))
				.flatMap(new Func1<String, Observable<String>>() {

					private AtomicInteger count = new AtomicInteger(0);

					public Observable<String> call(final String s) {
						final int i = count.incrementAndGet();
						return Observable.create(new OnSubscribe<String>() {
							public void call(Subscriber<? super String> subscriber) {
								System.out.println(
										"i:" + i + ";url:" + s + ",thread:" + Thread.currentThread().getName());
								subscriber.onNext(i + "");
								subscriber.onCompleted();
							}
						}).subscribeOn(Schedulers.io());
					}
				}).subscribe(new Subscriber<String>() {

					public void onCompleted() {

					}

					public void onError(Throwable e) {
						e.printStackTrace();
					}

					public void onNext(String t) {
						System.out.println("consumer:" + t + ",thread:" + Thread.currentThread().getName());
					}
				});
	}

}
