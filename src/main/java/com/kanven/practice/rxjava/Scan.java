package com.kanven.practice.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

public class Scan {
	

	public static void main(String[] args) {
		Observable.just(1, 2, 3, 4, 5).scan(new Func2<Integer, Integer, Integer>() {

			public Integer call(Integer sum, Integer item) {
				return sum + item;
			}

		}).subscribe(new Subscriber<Integer>() {

			public void onCompleted() {
				
			}

			public void onError(Throwable e) {
				
			}

			public void onNext(Integer t) {
				System.out.println("===next:"+t);
			}
		});
	}

}
