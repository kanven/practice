package com.kanven.practice.rxjava;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class Map {

	public static void main(String[] args) {
		Observable.from(new String[] { "this", "is", "rxjava" }).map(new Func1<String, String>() {

			public String call(String t) {
				return t.toUpperCase();
			}
		}).toList().map(new Func1<List<String>, List<String>>() {

			public List<String> call(List<String> t) {
				Collections.reverse(t);
				return t;
			}
		}).subscribe(new Action1<List<String>>() {

			public void call(List<String> words) {
				for (String word : words) {
					System.out.println(word);
				}
			}

		});
	}

}
