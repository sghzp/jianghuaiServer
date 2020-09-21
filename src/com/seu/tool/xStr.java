package com.seu.tool;

import com.sun.corba.se.spi.orbutil.fsm.FSMImpl;
import org.apache.camel.language.Simple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class xStr {
    public static String format(String var0, Object... var1) {
        return a(var0, var1);
    }

    protected static String a(String var0, Object... var1) {
        if(var0 != null && !var0.equals("")){
            for(int var2 = 0; var2 < var1.length; ++var2) {
                var0 = var0.replace("{" + var2 + "}",var1[var2].toString());
            }
            return var0;
        }else {
            return var0;
        }
    }


    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;
    public static boolean isSameDay(final long ms1, final long ms2){
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void main(String[] args) {
//        try {
//            System.out.println(isSameDay(simpleDateFormat.parse("2019-12-19 10:10:00").getTime(), simpleDateFormat.parse("2019-12-18 17:59:59").getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


}
