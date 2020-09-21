/*
 * 构建静态类 road_info数组，大小400
 * 使用road_info.c[0].xxx
 * */

package com.seu.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

public class road_info {

	public final static CopyOnWriteArrayList<road_data>  road_data_info= new CopyOnWriteArrayList<road_data>();
	//public static int gps;
	//public static int speed2;
	//public static int gps2;
	static{
		//进行数据库操作。读取road_info数组
		//c[0] = new road_data(30, 10.2);
		DBO db = new DBO();//初始链接数据库类
		ResultSet rs = null;
		String sql = "select * from tb_road_point order by p_road";//查询 通知sql语句，；
//		db.open();//打开数据库链接
		try {
			rs = db.query(sql);
			while(rs.next()){
				road_data r = new road_data();
				r.setId(rs.getInt("id"));
				r.setRoad_name(rs.getString("road_name"));
				r.setP_name(rs.getInt("p_name"));
				r.setP_road(rs.getInt("p_road"));
				r.setLat(rs.getDouble("lat"));
				r.setLon(rs.getDouble("lon"));
				/*r.setSpeed_max(rs.getInt("speed_max"));
				r.setSpeed_min(rs.getInt("speed_min"));*/
/*				r.setSpeed_max(60);
				r.setSpeed_min(20);*/
				road_data_info.add(r);
			}
			//输出容器的长度
			System.out.println("容器的大小为："+road_data_info.size());	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//查询结果
		finally{
			db.close();
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
}
