package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExceptionDao {
	public void insertException(String carName,String time,double inter){
		String sql = "insert into exception (carName,time,inter) values (?,?,?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, carName);
			preparedStatement.setString(2, time);
			preparedStatement.setDouble(3, inter);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
		
	}
}
