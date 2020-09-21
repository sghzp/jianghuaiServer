package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class CarInfoDao extends DAO {

	// INSERT所有元素
	public void updateAll(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
System.out.println("object_num:" + args.length);
			for (int i = 0; i < args.length; i++) {

				if(args[i] instanceof Date) {
					args[i] = new java.sql.Timestamp(((Date) args[i]).getTime());
				}
				preparedStatement.setObject(i + 1, args[i]);
			}

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}

	// 插入所有的数据进入其中，按照对象插入;
	public void InsertAll(String tablename, Object... obj) {
		String sql = "insert into " + tablename + "(time,longitude,latitude,gPS_state,gPS_speed,heading,"
				+ "airtemp,airstress,inairtemp,inairstress,risestress,oilstress,carspeed,enginespeed,enginetorque,drivertorque,"
				+ "footplate,engineload,fastconsumption,averageconsumption,consumptionrate,clutch,stop,engineneedtorque,enginetorquemode,frictiontorque,"
				+ "firestress,firetemp,oiltemp,coldtemp,voltage,enginetemp,gaugespeed,frontspeed,changegear,outair,"
				+ "vehicleID,deviceID,sum,mysum,check_result,systime,inter,mile,road_type,INSTRUMENT_ID,PROJECT_NUM,PROJECT_TASK_ID) values("
				+ "?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?)";
		updateAll(sql, obj);
	}
}
