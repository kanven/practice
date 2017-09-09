package com.kanven.practice.rxjava;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

public class Defer {

	private static class Inner {

		private int real;

		public Inner(int real) {
			this.real = real;
		}

		public int getReal() {
			return real;
		}

		public void setReal(int real) {
			this.real = real;
		}

	}

	public static void main(String[] args) {
		final Inner inner = new Inner(10);
		Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {

			public Observable<String> call() {
				return Observable.just("the real value:" + inner.getReal());
			}

		});
		inner.setReal(101);
		observable.subscribe(new Action1<String>() {

			public void call(String t) {
				System.out.println(t);
			}

		});
	}

}
