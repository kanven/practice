package com.kanven.practice.rxjava;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class Buffer {

	public static void main(String[] args) {
		Observable.just(1, 2, 3, 4, 5, 6, 7, 8).buffer(3).subscribe(new Action1<List<Integer>>() {

			public void call(List<Integer> items) {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (int i = 0, len = items.size(); i < len; i++) {
					sb.append(items.get(i));
					if (i < len - 1) {
						sb.append(",");
					}
				}
				sb.append(")");
				System.out.println(sb);
			}

		});
	}

}
