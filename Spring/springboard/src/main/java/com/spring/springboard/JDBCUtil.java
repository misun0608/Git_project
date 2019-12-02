package com.spring.springboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {
	public static Connection getConnection() {	// static이라 객체만들필요 없음
		Connection conn = null;
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String oraId = "scott";
			String oraPw = "123456";
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, oraId, oraPw);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	// close 작업
	public static void closeResource(Statement pstmt, Connection conn) {
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (Exception e) {
			pstmt = null;
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			conn = null;
		}
	}
	
	// close 작업 오버로딩
	public static void closeResource(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if(rs != null)
				rs.close();
		}catch(Exception e) {
			rs = null;
		}
		try {
			if(pstmt != null)
				pstmt.close();
		}catch(Exception e) {
			pstmt = null;
		}
		try {
			if(conn != null)
				conn.close();
		}catch(Exception e) {
			conn = null;
		}
	}
}
