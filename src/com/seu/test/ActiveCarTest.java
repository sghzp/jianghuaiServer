package com.seu.test;

import com.seu.database.ActiveCarDao;

public class ActiveCarTest {

	public static void main(String[] args) {
		String tablename = "aaa";
		ActiveCarDao activeCarDao = new ActiveCarDao();
		System.out.println(activeCarDao.hasCar(tablename));

	}

}
