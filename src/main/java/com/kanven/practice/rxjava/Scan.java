package com.kanven.practice.rxjava;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.Subscriber;

public class Scan {

	public static void main(String[] args) {
		/*Observable<Integer> observable = Observable.create(new OnSubscribe<Integer>() {

			public void call(Subscriber<? super Integer> subscriber) {
				for (int i = 1; i <= 5; i++) {
					subscriber.onNext(i);
				}
				subscriber.onCompleted();
			}
		});*/
		Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
		observable.scan(new Func2<Integer, Integer, Integer>() {

			public Integer call(Integer sum, Integer item) {
				return sum + item;
			}

		});
		observable.subscribe(new Subscriber<Integer>() {

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
