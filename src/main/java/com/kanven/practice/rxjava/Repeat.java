package com.kanven.practice.rxjava;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;

public class Repeat {

	public static void main(String[] args) {
		Observable<Integer> observable = Observable.create(new OnSubscribe<Integer>() {

			public void call(Subscriber<? super Integer> subscriber) {
				subscriber.onNext(101);
				subscriber.onCompleted();
			}

		}).repeat();
		observable.subscribe(new Action1<Integer>() {

			public void call(Integer t) {
				System.out.println("====" + t);
			}

		});
	}

}
