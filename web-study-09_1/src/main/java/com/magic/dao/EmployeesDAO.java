package com.magic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.magic.dto.EmployeesVO;

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

	//id, pass 전달받아서, DB랑 연동해서 데이터가 있는지 조회
	public int userCheck(String id, String pass, String lev) {
		
		/*
		 * -1: 아이디가 존재하지 않을떄
		 * 0 : 비밀번호가 맞지 않을떄
		 * 1 : 레벨이 맞지 않을때, 권한없음
		 * 2 : 관리자 로그인
		 * 3 : 일반회원 로그인
		 */
		
		int result = -1; //result 초기값 -1로 설정
		
		String sql = "select pass, lev from EMPLOYEES where id = ?";
		
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, id);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String dbPass = rs.getString("pass");
	            String dbLev = rs.getString("lev");

	            if (dbPass.equals(pass)) {
	            	if (!dbLev.equals(lev)) {
	                    result = 1; // 사용자가 선택한 레벨과 다름
	                } else if (dbLev.equals("A")) {
	                    result = 2;
	                } else if (dbLev.equals("B")) {
	                    result = 3;
	                } else {
	                    result = 1; // 권한 없음
	                }
	            } else {
	                result = 0; // 비밀번호 틀림
	            }
	        } else {
	            result = -1; // 아이디 없음
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return result;
	} //userCheck(String id, String pass)
	
	
	// 아이디를 조건으로 회원정보를 조회
	public EmployeesVO getEmployees(String id) {
		
		EmployeesVO eVo = null;
		
		String sql = "select * from employees where id = ? ";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String userid = rs.getString("id");
				String pass = rs.getString("pass");
				String name = rs.getString("name");
				String lev = rs.getString("lev");
				String gender = rs.getString("gender");
				String phone = rs.getString("phone");
				
				eVo = new EmployeesVO();
				eVo.setId(id);
				eVo.setPass(pass);
				eVo.setName(name);
				eVo.setLev(lev);
				eVo.setGender(gender != null ? Integer.parseInt(gender) : null);
				eVo.setPhone(phone);
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
		
		
		return eVo;
	} // getEmployees(String id)
	
	
	//저장
	public int insertMember(EmployeesVO eVo) {

		int result = -1; //result 초기값 -1로 설정
		/* -1로 초기화 이유 => 예외 상황 대비
			 try 안의 로직이 실패하거나, DB 연결이 실패할 경우
			→ pstmt.executeUpdate()는 호출되지 않음 
			→ 그래서 초기값 -1은 "예외나 로직 실패"를 의미하는 안전한 디폴트값
		 */
			
		Connection conn = null;
		PreparedStatement pstmt = null;
			
		String sql = "insert into employees(id, pass, name, lev, gender, phone) values (?, ?, ?, ?, ?, ?)";
			
		try {
			//1. DB 연결
			conn = getConnection();
			//2. sql구문 전송
			pstmt = conn.prepareStatement(sql);
			
			// ?값에 세팅
			pstmt.setString(1, eVo.getId());
			pstmt.setString(2, eVo.getPass());
			pstmt.setString(3, eVo.getName());
			pstmt.setString(4, eVo.getLev());
			pstmt.setString(5, eVo.getGender() != null ? eVo.getGender().toString() : "1");
			pstmt.setString(6, eVo.getPhone());
				
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

	
	public void updateMember(EmployeesVO eVo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
			                          //     1      2       3       4        5             6
		String sql = "update employees set pass=?, name=?, lev=?, gender=?, phone=? where id=?";
			
		try {
			//1. DB 연결
			conn = getConnection();
			//2. sql구문 전송
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, eVo.getPass());
			pstmt.setString(2, eVo.getName());
			pstmt.setString(3, eVo.getLev());
			pstmt.setString(4, eVo.getGender() != null ? eVo.getGender().toString() : "1");
			pstmt.setString(5, eVo.getPhone());
			pstmt.setString(6, eVo.getId());
				
			/* 3. sql 구문 실행
				executeUpdate -> insert, update, delete 시 사용
				result : 0 -> 저장 실패
				result : 1 -> 저장 성공
				commit은 auto commit;
			*/
			
			pstmt.executeUpdate();
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
		
	} // end updateMember
		
}
