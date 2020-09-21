package moniClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

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
//					Socket socket = new Socket("218.92.163.174", 10002);
				Socket socket = new Socket("127.0.0.1", 10002);
				PrintStream ps = new PrintStream(socket.getOutputStream());
				InputStream is=socket.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));

				double lat =32.05877181930;
				// double count = 170712.00;
				while (true) {
					if(lat >=32.05877181930 && lat<35.05877181930){
						lat=lat + 0.5;
					}else{
						lat =32.05877181930;
					}
					double lon = (Math.random() + 0.63797246638887)/2*60 + 12000;
					lat = (Math.random() + 0.23795955194445)/2*60 + 3300;
//					double lon = (0.637961605 + 0.63797246638887)/2*60 + 12000;
//					lat = (0.23801028583335 + 0.23795955194445)/2*60 + 3300;
//					String s = "CATARCD," + Thread.currentThread().getName() + ",OX01,034428181017,33.241042950550000,N,120.635612907610000,E,0,0.0033,167.755519,Y,*50";
					String s = "CATARCD," + Thread.currentThread().getName() + ",OX01,034428181017,"+lat+",N,"+lon+",E,0,0.0033,167.755519,Y,*50";
					s = "$" + s;//+ count
					String check = getXor(s.getBytes());
					count++;
					ps.print(s);
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
		Client cl = new Client();
		for (int i = 0; i < 100; i++) {
			new Thread(cl, "socket connect " + i).start();
		}

	}

}
