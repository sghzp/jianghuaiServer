package moniClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Clientpoint implements Runnable{

    static double count = 1458985738762.0;
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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
					Socket socket = new Socket("139.224.195.10", 60001);
//            Socket socket = new Socket("127.0.0.1", 10002);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            double lat =32.05877181930;
            // double count = 170712.00;
            String ss = "$2019-05-03 20:09:43,118.78864856492,32.05843489081,SINGLE,0.0481,170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID1,ZJ-DY-DVR-10-03-20150817,25#\n";
            while (true) {
                double lon = (Math.random()-0.5) + 12038.2479647;
                lat = (Math.random()-0.5) + 3314.958697;
//					double lon = (0.637961605 + 0.63797246638887)/2*60 + 12000;
//					lat = (0.23801028583335 + 0.23795955194445)/2*60 + 3300;
//					String s = "CATARCD," + Thread.currentThread().getName() + ",OX01,034428181017,33.241042950550000,N,120.635612907610000,E,0,0.0033,167.755519,Y,*50";
                String s = "CATARCD," + Thread.currentThread().getName() + ",OX01,034428181017,"+lat+",N,"+lon+",E,0,0.0033,167.755519,Y,*50";
                s = "$" + s;//+ count
                String check = getXor(s.getBytes());
                count++;
                ps.print(ss);
                System.out.println(br.read());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Clientpoint cl = new Clientpoint();
            new Thread(cl, "socket connect ").start();
    }

}
