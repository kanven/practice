package com.kanven.practice.rxjava;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class Map2 {

	public static void main(String[] args) {
		Observable.just("http://www.baidu.com/", "http://www.google.com/", "https://www.bing.com/")
				.map(new Func1<String, String>() {

					public String call(final String url) {
						try {
							return url + " " +getHostByUrl(url);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						return "";
					}
					
				}).subscribe(new Action1<String>() {

					public void call(String ip) {
						System.out.println(ip+",thread:"+Thread.currentThread().getName());
					}
					
				});
	}

	private static String getHostByUrl(String url) throws MalformedURLException, UnknownHostException {
		URL u = new URL(url);
		String host = u.getHost();
		String address = InetAddress.getByName(host).toString();
		int index = address.indexOf("/");
		return address.substring(index + 1);
	}

}
