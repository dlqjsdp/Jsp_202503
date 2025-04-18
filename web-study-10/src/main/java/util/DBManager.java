package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager { 
//DBManager는 DB 연결과 자원 해제를 편하게 해주는 재사용 가능한 도우미 클래스
//JDBC 코드에서 반복되는 부분을 추상화해서 중복을 줄이기 위한 목적
	
	public static Connection getConnection() { 
		//DB 연결을 생성해서 반환하는 **정적 메서드**  
		//어디서든 `DBManager.getConnection()`으로 호출할 수 있음
		
		Connection conn = null;
	
		String url = null;
		String uid = null;
		String pass = null;

		try {
			url = "jdbc:oracle:thin:@localhost:49161:xe";
			uid = "system";
			pass = "oracle";			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return DriverManager.getConnection(url,uid,pass);
		}catch(Exception e) {
			e.printStackTrace();
		}
			
		return null; 
	} //end getConnection
	 
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection conn, Statement stmt) {
		try {			
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

