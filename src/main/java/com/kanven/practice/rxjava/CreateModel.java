package com.kanven.practice.rxjava;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.observables.AsyncOnSubscribe;

public class CreateModel {

	public static void main(String[] args) {
		/*Observable<Integer> observable = Observable.create(new OnSubscribe<Integer>() {
			public void call(Subscriber<? super Integer> subscriber) {
				for (int i = 0; i < 10; i++) {
					subscriber.onNext(i);
				}
				subscriber.onCompleted();
			}
		});*/
		/*Observable<Integer> observable = Observable.create(new SyncOnSubscribe<String,Integer>() {

			@Override
			protected String generateState() {
				return "state";
			}

			@Override
			protected String next(String state, Observer<? super Integer> observer) {
				observer.onNext(1);
				observer.onCompleted();
				return "state";
			}
		});*/
		Observable<Integer> observable = Observable.create(new AsyncOnSubscribe<String,Integer>() {

			@Override
			protected String generateState() {
				return null;
			}

			@Override
			protected String next(String state, long requested, Observer<Observable<? extends Integer>> observer) {
				observer.onNext(Observable.create(new OnSubscribe<Integer>() {

					public void call(Subscriber<? super Integer> subscriber) {
						subscriber.onNext(1);
						subscriber.onCompleted();
					}
				}));
				observer.onCompleted();
				return null;
			}
		});
		observable.subscribe(new Subscriber<Integer>(){

			public void onCompleted() {
				System.out.println("====complete=====");
			}

			public void onError(Throwable e) {
				
			}

			public void onNext(Integer t) {
				System.out.println("======="+t);
			}
			
		});
	}

}
