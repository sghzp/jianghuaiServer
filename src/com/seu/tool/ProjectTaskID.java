package com.seu.tool;

import com.seu.database.JDBCTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProjectTaskID {
    @SuppressWarnings("finally")
    public static String ProjectTaskID(String DeviceID) {
        String Project_Task_ID = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT a.Project_Task_ID FROM tb_taskinfo a , tb_project_equipment b WHERE  a.PROJECT_TASK_ID = b.PROJECT_TASK_ID AND GETDATE()>STARTTIME AND TASK_STATE = 1 AND EQUIPMENTID='" + DeviceID + "';";
//System.out.println(sql);
        try {
            conn = JDBCTools.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getObject("Project_Task_ID") != null)
                    Project_Task_ID = rs.getString("Project_Task_ID");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                return Project_Task_ID;
            }

        }
    }
}







	/*






		double mile1 = 0;
		double mile2 = 0;
		double mile3 = 0;
		double mile4 = 0;
		double mile5 = 0;
		double mile6 = 0;
		double mile7 = 0;
		Connection conn0 = null;
		PreparedStatement ps0 = null;
		ResultSet rs0 = null;
		String sql1 = "select mile from ID1 order by id desc limit 1";
		String sql2 = "select mile from ID2 order by id desc limit 1";
		String sql3 = "select mile from ID3 order by id desc limit 1";
		String sql4 = "select mile from ID4 order by id desc limit 1";
		String sql5 = "select mile from ID5 order by id desc limit 1";
		String sql6 = "select mile from ID6 order by id desc limit 1";
		String sql7 = "select mile from ID7 order by id desc limit 1";
		try {
			conn0 = JDBCTools.getConnection();
			if(!JDBCTools.HasTable("ID1")){
				String sql = "create table ID1(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql1);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile1 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID2")){
				String sql = "create table ID2(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql2);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile2 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID3")){
				String sql = "create table ID3(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql3);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile3 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID4")){
				String sql = "create table ID4(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql4);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile4 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID5")){
				String sql = "create table ID5(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql5);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile5 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID6")){
				String sql = "create table ID6(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql6);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile6 = rs0.getDouble("mile");
			}
			if(!JDBCTools.HasTable("ID7")){
				String sql = "create table ID7(id int auto_increment primary key,"
						+ " time double," + " longitude double," + " latitude double," + " gPS_speed double,"
						+ " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
						+ " inairstress double," + "risestress double," + " oilstress double,"
						+ " carspeed double," + " enginespeed double," + " enginetorque double,"
						+ " drivertorque double," + " footplate double," + " engineload double,"
						+ " fastconsumption double," + " averageconsumption double,"
						+ " consumptionrate double," + " clutch double," + " stop double,"
						+ " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
						+ "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
						+ " voltage double," + " enginetemp double," + " gaugespeed double,"
						+ " frontspeed double," + " changegear double," + " outair double,"
						+ " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
						+ " mysum varchar(30)," + " check_result boolean," + " systime timestamp,"
						+ " inter double," + " mile double);";

				JDBCTools.createTable(sql);
			}
			ps0 = conn0.prepareStatement(sql7);
			rs0 = ps0.executeQuery();
			if(rs0.next()){
				if(rs0.getObject("mile") != null)
				mile7 = rs0.getDouble("mile");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs0.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ps0.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn0.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}




















































//System.out.println(tablename+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
System.out.println("经纬度"+ longitude + "#" + latitude + "#"+gPS_speed + "#" + carspeed);
		String id = null;
		String warningTablename = null;
		switch(tablename){
		case "ID1":warningTablename = "gis_show1";id = "ID1";break;
		case "ID2":warningTablename = "gis_show2";id = "ID2";break;
		case "ID3":warningTablename = "gis_show3";id = "ID3";break;
		case "ID4":warningTablename = "gis_show4";id = "ID4";break;
		case "ID5":warningTablename = "gis_show5";id = "ID5";break;
		case "ID6":warningTablename = "gis_show6";id = "ID6";break;
		case "ID7":warningTablename = "gis_show7";id = "ID7";break;
		default:break;
		}
		double local_speed = 0;
		if(carspeed == -1 || carspeed == 0 || carspeed ==255){
				if(gPS_speed > 1){
					local_speed = gPS_speed*3.6;
				}
			}else{
				local_speed = carspeed;
			}
		 ******************************************************************
		*1、point_info中的信息已经存储在了静态数据中。需要将传递过来的位置信息做搜索。
		* 通过传入车辆当前位置信息local_position_lon,local_position_lat,遍历路段点
		* 静态数据来确定属于的道路类型。
		* 输入参数：local_position_lon，local_position_lat
		* 输出参数：road_type
		******************************************************************
		int i = 0 ;//遍历循环量
		int flag = 0; //如果找到了目标值就要置位。
		int road_type = 0;//道路类型。
		int road_segment_a =0;
		int road_type_a = 0;
		int road_segment_b =0;
		int road_type_b = 0;
		int warning_type = 0;
		int warningCount = 0;
		boolean isonroad = false;
		double road_longitude = 0;
		double road_latitude = 0;
		ArrayList<road_data> arr = road_info.road_data_info;
		ArrayList<Double> polygonXA = new ArrayList<Double>();
		ArrayList<Double> polygonYA = new ArrayList<Double>();
		int length = arr.size();
		map_point_judge mpj = new map_point_judge();
		//1、首先获取每个数据点的道路段ID和道路ID，如果一直一致，那么就存入list中。
		for(int j = 0 ;j <= (length-2); j++)
		{

			 road_segment_a = arr.get(j).p_name;
			 road_segment_b = arr.get(j+1).p_name;
			 road_type_a = arr.get(j).p_road;
			 road_type_b = arr.get(j+1).p_road;
			 road_longitude = arr.get(j).lon;
			 road_latitude = arr.get(j).lat;
			 if((road_segment_a == road_segment_b)&&(road_type_a == road_type_b))
			 {
			    //将当前的路段位置信息存入链表
				 polygonXA.add(road_longitude); //经度
				 polygonYA.add(road_latitude); //纬度

			 }
			 else
			 {
				 polygonXA.add(road_longitude); //经度
				 polygonYA.add(road_latitude); //纬度
				 if(j == (length-2))
				 {
					 polygonXA.add(arr.get(j+1).lon); //经度
					 polygonYA.add(arr.get(j+1).lat); //纬度
					 System.out.println(j);
				 }
			   //根据目前的position以及road_judge
				 isonroad = mpj.isPointInPolygon(longitude,latitude,polygonXA,polygonYA);
				 	if(isonroad)
				    {
				 		System.out.println("获取的路段是"+road_type_a);
				 		road_type =  road_type_a;
				    	if(road_type == 18)
					    {
				    		warning_type = 2;
					    }
				    	else if(local_speed > arr.get(j).speed_max
				    		|| local_speed < arr.get(j).speed_min)
				    	{
				    		warning_type = 1;
				    	}
				    	flag = 1;//获取所在道路类型
				    	break;
				    }
			   //判断不在该路段则要进行清空操作。
			   polygonXA.clear();
			   polygonYA.clear();
			 }
			 }

		if(flag == 0 )
		{
			System.out.println("未获取到相应路段");
		}
		*//*******************************************************************
 * 3、把告警信息写入到相应的gis_shown 数据库中
 * current_road、overspeed、if_overspeed、if_worryway、warning_time
 * 这里需要把road_type，warning_type返回。
 * 输入：warning_type,road_type,local_speed,local_time,vehicle_id
 * 输出：current_road、overspeed、if_overspeed、if_worryway、warning_time
 *******************************************************************//*
		if (1 == flag)
		{
			System.out.println("进入报警插值");
			boolean tableflag = JDBCTools.HasTable(warningTablename);
			if(!tableflag){
				String sql = "create table " + warningTablename + "(warning_id int auto_increment primary key,"
						+ " current_road int," + " pre_road int," + " if_overspeed int," + " if_worryway int,"
						+ " warning_time Timestamp," + " overspeed double," + " warning_count int," + " time  int);";
System.out.println(sql);
				JDBCTools.createTable(sql);
			}

			//把告警信息写到GIS_SHOUW数据表中。
			if(warning_type == 1 || warning_type == 2){
				Connection conn = null;
				PreparedStatement ps = null;
				Connection conn1 = null;
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;




			try {
				conn = JDBCTools.getConnection();
				conn1 = JDBCTools.getConnection();
				String sql = "insert into " + warningTablename + "(current_road,pre_road,if_overspeed,if_worryway,warning_time,overspeed,warning_count,time)values(?,?,?,?,?,?,?,(SELECT TIMESTAMPDIFF(SECOND,(SELECT starttime FROM testguanli WHERE recorder = '" + id + "' ORDER BY id DESC LIMIT 1),DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s'))))";
				String sql1 = "select warning_count from " + warningTablename + " order by warning_id desc limit 1";
				System.out.println(sql);
				ps = conn.prepareStatement(sql);
//				ps.setString(1, 'gis_show4');
				ps1 = conn1.prepareStatement(sql1);
//				ps1.setString(1, warningTablename);
				rs1 = ps1.executeQuery();
				修改报警次数
				rs1.last();
				if(rs1.getRow()  > 0){
					warningCount = rs1.getInt("warning_count");

				}else{
					warningCount = 0;
//System.out.println("warning_type:"+warning_type+"else----------------" + warningCount);
				}



				//取tablename(gis_show1---gis_show6)（String），取六个字段的值（int,int,int,int,String,double）。
				//ps.setString(1, "gis_show1");
				ps.setInt(1, road_type);
				ps.setInt(2, 0);
				if(warning_type == 1)
				{
					ps.setInt(3, 1);
					ps.setInt(4, 0);
				}else if (warning_type == 2)
				{
					ps.setInt(3, 0);
					ps.setInt(4, 1);
				}
				ps.setObject(5,local_time);
				ps.setDouble(6,local_speed);
				ps.setInt(7,warningCount);
			//	System.out.println("sql"+sql);
			//	System.out.println("sql1"+sql1);

				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				JDBCTools.free(null, ps, conn);
				JDBCTools.free(rs1, ps1, conn1);
			}
		}
		}
		//****if结束，将“超速信息”插入数据库操作完成。*********


	}
}
*/
