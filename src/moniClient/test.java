package moniClient;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by fangyang on 2017/10/10.
 */
public class test {
    public static void main(String[] args) {
        File f = new File("g:/");
        if(f.exists()) {
            System.out.println("111111");
        }
        System.out.println("".equals(""));
        String str = "CATARCD,A123456,OX01,125532171017,3314.5957374,N,12037.6969526,E,000.0,000.000,2,*";
//        System.out.println(str.length());
//        System.out.println(stringToHexString(str).length());
//        System.out.println(stringToHexString("a"));
        System.out.println(getXor(str.getBytes()));
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        String time = "234511200817";
//        int hour = Integer.parseInt(time.substring(0,2)) + 8;
//        if(hour > 23){
//            hour -= 24;
//        }
//
//        time = "20" + time.substring(10,12) + "-" + time.substring(8,10) + "-" +
//                time.substring(6,8) + " " + Integer.toString(hour) + ":" +
//                time.substring(2,4) + ":" + time.substring(4,6);
//        System.out.println(time);
//        try
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = sdf.parse(time);
//            System.out.println(new Timestamp(date.getTime()));
//        }
//        catch (ParseException e)
//        {
//            System.out.println(e.getMessage());
//        }

        }

        /**
         * 异或运算，用于异或校验
         */
        public static String getXor(byte[] datas){
//        System.out.println(datas.length);
            byte temp=datas[0];
            for (int i = 1; i <datas.length; i++) {
                temp ^=datas[i];
//            System.out.println(datas[i]);
            }
            String check = Integer.toHexString(temp);
            return check;
        }


    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
