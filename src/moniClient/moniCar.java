package moniClient;

import org.apache.activemq.thread.SchedulerTimerTask;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class moniCar implements Runnable {

    static double count = 1458985738762.0;
    static int jiange = 500;
    /**
     * 异或运算，用于异或校验
     */
    public static String getXor(byte[] datas) {
//        System.out.println(datas.length);
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
//            System.out.println(datas[i]);
        }
        String check = Integer.toHexString(temp);
        return check;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
//          Socket socket = new Socket("172.25.202.11", 60001);
            Socket socket = new Socket("139.224.195.10", 60001);
            PrintStream ps = new PrintStream(socket.getOutputStream());

            double lat = 32.05877181930;
            String ss = "$2019-05-03 20:09:43,118.78864856492,32.05843489081,SINGLE,0.0481,170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID1,ZJ-DY-DVR-10-03-20150817,25#";

            int x = 0;
            String line;
            String str[] = new String[2];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pathname = "D:\\InteliJProjects\\MainProjects\\jianghuai\\jianghuai\\jianghuaiRoads\\轻型坑洼路_8805_8810.txt";
            String ctime;
            while (true) {
                FileReader reader = new FileReader(pathname);
                BufferedReader br1 = new BufferedReader(reader);
                while ((line = br1.readLine()) != null) {
                    x++;
                    if(x == 139)x=1;
                    ctime = simpleDateFormat.format(new Date());
                    System.out.println(ctime);
                    str = line.split(",");
                    lat = Double.parseDouble(str[1]);
                    double lon = Double.parseDouble(str[0]);
//                    ss = "$"+ctime+"," + str[0] + "," + str[1] + ",SINGLE,"+ 100 +",170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID70000005,ZJ-DY-DVR-10-03-20150817,25#";
                    ss = "$"+ctime+"," + "117.1992278,31.69018173" + ",SINGLE,"+ 0.0 +",170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID70000005,ZJ-DY-DVR-10-03-20150817,25#";
                    //String ss1 = "$"+ctime+"," + "117.1992278,31.69018173" + ",DOUBLE,"+ 0.0 +",170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID70000004,ZJ-DY-DVR-10-03-20150817,25#";
//                    ss = "$"+ctime+"," + "117.199155900000000,31.690137210000000" + ",SINGLE,"+ 1000 +",170.060039,2000,10150,8500,51000,9000,-100,0,0,0,0,0,0,12549,0,0,0,100,0,0,1300,-100,21500,177496,-2500,1194,177496,-100,0,11400,-100,ID70000005,ZJ-DY-DVR-10-03-20150817,25#";
                    count++;
                    ps.print(ss);
                    //ps.print(ss1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String peizhi ="D:\\模拟发送配置.txt";
        FileReader reader = null;
        int geshu = 1;
        int pinlv = 500;
        try {
            reader = new FileReader(peizhi);
            BufferedReader br1 = new BufferedReader(reader);
            geshu = Integer.parseInt(br1.readLine());
            pinlv = Integer.parseInt(br1.readLine());
            jiange = Integer.parseInt(br1.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        moniCar cl = new moniCar();
        try {
            for (int i = 0; i < geshu; i++) {
                new Thread(cl, "socket connect "+i).start();
//                Thread.sleep(pinlv);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 读入TXT文件
     */
    public static void readFile() {
        String pathname = "C:\\Users\\zfz\\Desktop\\高速环道2.txt";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
