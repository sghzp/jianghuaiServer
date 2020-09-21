package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActiveCarDao {
	
	public boolean hasCar(String carname){
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "select * from activecar where carName = ?";
		try {
			conn = JDBCTools.getConnection();
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, carname);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				return true;
		} catch (Exception e) {
			System.err.println("ActiveCAR获取有问题");
		}finally{
			JDBCTools.releaseDB(resultSet, preparedStatement, conn);
		}
		return false;
		
	}
	
	public void insert(String tablename){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "insert into activecar (carName) values (?)";
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tablename);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
