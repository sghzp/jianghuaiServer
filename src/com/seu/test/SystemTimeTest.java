package com.seu.test;

import java.sql.Timestamp;

public class SystemTimeTest {

	public static void main(String[] args) {
		
		//Date dt = new Date();
		//System.out.println(dt.getTime());
		//System.out.println(dt.getSeconds());
		//long ll = dt.getTime();
		Timestamp d = new Timestamp(System.currentTimeMillis());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp d2 = new Timestamp(System.currentTimeMillis());
		System.out.println(d2.getTime()-d.getTime());
		System.out.println(d2.toString());
		float a = 100/1000;
		System.out.println(a);
	}

}
