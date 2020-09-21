package com.seu.test;

import com.seu.database.JDBCTools;

public class JieXiDtuData {
	public static void main(String[] args) {
		String content = "$170712.00,40.06729383406,116.17804770735,0.000,0.00,0,0,12549,0,-100,FF,FF,FF,FF,FF,12345,ZJ-SC-DVR-10-03-20141230,107#";
		if(content.startsWith("$")&&content.endsWith("#")){
			String substr = content.substring(1,content.length()-1);
			//System.out.println(substr);
			String[] divide_str = substr.split(",");
			for(int k=0;k<divide_str.length;k++){
				System.out.println(k+"-----"+divide_str[k]);;
			}
			
			//计算校验和;
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<divide_str.length-1;i++){
				if(i!=divide_str.length-2){
					sb.append(divide_str[i]);
					sb.append(",");
				}else{
					sb.append(divide_str[i]);
				}
			}
			byte[] check = sb.toString().getBytes();
			//char[] ch = sb.toString().toCharArray();
			int sum_check = 0;
			for(int k=0;k<check.length;k++){
				//System.out.println("ch"+ch[k]+":"+check[k]);
				sum_check+=check[k];
			}
			int check_date = (byte)sum_check;
			System.out.println("校验和"+sum_check+":"+(byte)sum_check);
			//进行校验对比，相等说明接收成功
			if(check_date==Integer.parseInt(divide_str[divide_str.length-1])){
				//校验成功了
				System.out.println("校验成功");
				String name = divide_str[15];//读取车辆名称;
				String tablename = "_"+name;
				boolean flag = JDBCTools.HasTable(tablename);
				System.out.println(flag);
				if(flag){
					System.out.println("直接插入值就好");
				}else{
					System.out.println("现创建表");
					String sql = "create table " + tablename
							+ "(id int auto_increment primary key,"
							+ " time double," + " longitude double,"
							+ " latitude double," + " GPS_speed double,"
							+ " heading double," + " OBD_speed int,"
							+ " OBD_EngineSpeed int,"
							+ " OBD_FuelConsumption int," + " Wheel_angle int,"
							+ "reserve1 varchar(30),"
							+ " reserve2 varchar(30),"
							+ " reserve3 varchar(30),"
							+ " reserve4 varchar(30),"
							+ " reserve5 varchar(30),"
							+ " reserve6 varchar(30),"
							+ " VehicleID varchar(30),"
							+ " DeviceID varchar(30)," + " sum varchar(30),"
							+" inter int);";
					JDBCTools.createTable(sql);
					System.out.println("创建好表再进行插入数据");
						
				}
			}else{
				System.out.println("校验失败");
			}
			
		}
	}

}
