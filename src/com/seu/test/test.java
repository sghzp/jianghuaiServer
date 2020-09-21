package com.seu.test;

import com.seu.tool.HttpClient;
import com.seu.tool.xStr;

public class test {

    public final static HttpClient httpClient = new HttpClient();

    public static void main(String[] args) {
        String url = "http://172.25.202.200/GPS/gpsWarring";
        String res = "time=\"{0}\"&"+"warning_type={1}&gPS_speed={2}&road_type={3}&longitude={4}&latitude={5}&road_name=\"{6}\"&orderid={7}&vin=\"{8}\"";
//        String res = "time=\"{0}\"&"+"warning_type={1}";
        res = xStr.format(res,"2019/12/6 00:00:00",1,20.0,35,120.0000,31.0000,"正弦波路",630,"ID600003");
        httpClient.doPost(url,res);
    }

}
