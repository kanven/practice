package com.kanven.practice.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Interval {

	public static void main(String[] args) {
		Observable<Long> observable = Observable.interval(1000, TimeUnit.MICROSECONDS,Schedulers.trampoline());
		observable.subscribe(new Subscriber<Long>() {

			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}

			public void onNext(Long t) {
				System.out.println(t);
			}
		});
	}
	
}
