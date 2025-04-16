package com.saeyan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.saeyan.dto.MemberVO;

public class EmployeesDAO {
	
	// DAO class는 싱글톤 클래스로 생성
	// 외부에서 접근할 수 있는 static 변수를 생성
	private static EmployeesDAO instance  = new EmployeesDAO();
	
	// 생성자를 private 제어자로 설정
	private EmployeesDAO() {
	}
	
	// 외부 클래스에서 EmployeeDAO 객체를 참조하도록 하는 메소드
	public static EmployeesDAO getInstance() {
		return instance;
	}
	
	
	//1. DB 연결
	public Connection getConnection() throws Exception{	
		
		String url = "jdbc:oracle:thin:@localhost:49161:xe";
		String uid = "system";
		String pass = "oracle";
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		//2. DB연결	    
		
		return DriverManager.getConnection(url,uid,pass);
	}

	//userid, pwd 전달받아서, DB랑 연동해서 데이터가 있는지 조회
	public int userCheck(String userid, String pwd) {
		
		/*
		 * -1: 아이디가 존재하지 않으면
		 * 0 : 비밀번호가 맞지 않으면
		 * 1 : 아이디, 레벨이 맞지 않으면
		 * 2 : 관리자 레벨로 로그인 했으면
		 * 3 : 일반회원으로 로그인하면
		 */
		
		int result = -1; //result 초기값 -1로 설정
		
		String sql = "select pwd from member where userid = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;  //sql 구문이 select일때만 기입!
		
		try {
			//1. DB연결
			conn = getConnection();
			//2. sql구문 정송
			pstmt = conn.prepareStatement(sql);
			//3. sql 맵핑
			pstmt.setString(1, userid);
			//4. sql 구문 실행
			rs = pstmt.executeQuery(); //sql 구문이 select일때만 기입!
			
			if(rs.next()) {
				//회원ID 존재!!
				if(rs.getString("pwd") != null && 
						rs.getString("pwd").equals(pwd)) {
					result = 1; // userid, pwd 일치
				}else {
					result = 0; // pwd만 불일치
				}
			}else {
				//이런 회원ID는 없다!
				result = -1;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if ( rs != null) rs.close();
				if ( pstmt != null) pstmt.close();
				if ( conn != null) conn.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public MemberVO getMember(String userid) {
		
		MemberVO mVo = null;
		
		String sql = "select * from member where userid = ? ";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("userid");
				String pwd = rs.getString("pwd");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				int admin = rs.getInt("admin");
				
				mVo = new MemberVO();
				mVo.setName(name);
				mVo.setUserid(id);
				mVo.setPwd(pwd);
				mVo.setEmail(email);
				mVo.setPhone(phone);
				mVo.setAdmin(admin);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if ( rs != null) rs.close();
				if ( pstmt != null) pstmt.close();
				if ( conn != null) conn.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return mVo;
	}
	
	public int confirmID(String userid) {
		int result = 1;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select userid from member where userid = ?";

		try {
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery(); 
			
			if(rs.next()) {
				result = 1; //아이디 존재 => 사용불가 아이디
			}else {
				result = -1; //사용가능 아이디
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if ( rs != null) rs.close();
				if ( pstmt != null) pstmt.close();
				if ( conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//저장
	public int insertMember(MemberVO mVo) {

		int result = -1; //result 초기값 -1로 설정
		/* -1로 초기화 이유 => 예외 상황 대비
			 try 안의 로직이 실패하거나, DB 연결이 실패할 경우
			→ pstmt.executeUpdate()는 호출되지 않음 
			→ 그래서 초기값 -1은 "예외나 로직 실패"를 의미하는 안전한 디폴트값
		 */
			
		Connection conn = null;
		PreparedStatement pstmt = null;
			
		String sql = "insert into member values(?, ?, ?, ?, ?, ?)";
			
		try {
			//1. DB 연결
			conn = getConnection();
			//2. sql구문 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mVo.getName());
			pstmt.setString(2, mVo.getUserid());
			pstmt.setString(3, mVo.getPwd());
			pstmt.setString(4, mVo.getEmail());
			pstmt.setString(5, mVo.getPhone());
			pstmt.setInt(6, mVo.getAdmin());
				
			/* 3. sql 구문 실행
				executeUpdate -> insert, update, delete 시 사용
				result : 0 -> 저장 실패
				result : 1 -> 저장 성공
				commit은 auto commit;
			*/
			
			result = pstmt.executeUpdate();	
			// pstmt.executeUpdate()의 결과가 성공이면 1, 실패면 0이 리턴됨

			/*
			  여기서도 -1은 초기 실패 상태로 가정
			  insertMember()는 성공/실패 여부만 리턴하면 되기 때문에
			  실제로 -1, 0, 1을 구분하는 용도는 아니고 단순 실패 대비용 초기값
			 */
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	} //end insertMember

	
	public void updateMember(MemberVO mVo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
			                          //     1      2       3       4        5             6
		String sql = "update member set name=?, pwd=?, email=?, phone=?, admin=? where userid=?";
			
		try {
			//1. DB 연결
			conn = getConnection();
			//2. sql구문 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mVo.getName());
			pstmt.setString(2, mVo.getPwd());
			pstmt.setString(3, mVo.getEmail());
			pstmt.setString(4, mVo.getPhone());
			pstmt.setInt(5, mVo.getAdmin());
			pstmt.setString(6, mVo.getUserid());
				
			/* 3. sql 구문 실행
				executeUpdate -> insert, update, delete 시 사용
				result : 0 -> 저장 실패
				result : 1 -> 저장 성공
				commit은 auto commit;
			*/
			
			int result = pstmt.executeUpdate();	
			// pstmt.executeUpdate()의 결과가 성공이면 1, 실패면 0이 리턴됨			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
		
}
