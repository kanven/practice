package com.kanven.practice.rxjava;

import java.io.Serializable;

import rx.Observable;
import rx.Observer;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Concat {

	private static class First implements Serializable {

		private static final long serialVersionUID = 8717343752898941894L;

		private String name;

		public First(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	private static class Second implements Serializable {

		private static final long serialVersionUID = 2491648432749848077L;
		
		private String name;

		public Second(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	public static void main(String[] args) {
		Observable<Object> first = Observable.create(new OnSubscribe<Object>() {
			public void call(Subscriber<? super Object> subscriber) {
				First f = new First("first");
				subscriber.onNext(f);
				subscriber.onCompleted();
			}
		}).subscribeOn(Schedulers.io());

		Observable<Object> second = Observable.create(new OnSubscribe<Object>() {

			public void call(Subscriber<? super Object> subscriber) {
				Second s = new Second("second");
				subscriber.onNext(s);
				subscriber.onCompleted();
			}

		}).subscribeOn(Schedulers.io());

		Observable.concat(first, second).subscribe(new Observer<Object>() {

			public void onCompleted() {

			}

			public void onError(Throwable e) {

			}

			public void onNext(Object t) {
				if (t instanceof First) {
					First first = (First) t;
					System.out.println(first.getName() + "  " + Thread.currentThread().getName());
				} else if (t instanceof Second) {
					Second second = (Second) t;
					System.out.println(second.getName() + "  " + Thread.currentThread().getName());
				} else {
					System.out.println("other...");
				}
			}
		});

	}

}
