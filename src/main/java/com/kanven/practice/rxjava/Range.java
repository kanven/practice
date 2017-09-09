package com.kanven.practice.rxjava;

import rx.Observable;
import rx.Observer;

public class Range {

	public static void main(String[] args) {
		Observable<Integer> observable = Observable.range(1,10);
		observable.subscribe(new Observer<Integer>() {

			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}

			public void onNext(Integer t) {
				System.out.println(t);
			}
		});
	}
	
}
