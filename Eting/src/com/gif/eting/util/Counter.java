package com.gif.eting.util;

public class Counter {
	private static Integer cnt = Integer.valueOf(0);
	private static CounterListener listner;

	public static void init() {
		Counter.cnt = Integer.valueOf(0);
	}

	public static Integer getCnt() {
		return cnt;
	}

	public void setCnt() {
		Counter.cnt++;
		if(Counter.cnt>=11){
			listner.onEvent(cnt);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(cnt);
	}

	public static void setListner(CounterListener listner) {
		Counter.listner = listner;
	}

}
