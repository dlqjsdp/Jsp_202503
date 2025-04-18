package com.saeyan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.saeyan.dto.ProductVO;

import util.DBManager;

public class ProductDAO { // DB와 실제 데이터를 주고받는 클래스

	private static ProductDAO instance  = new ProductDAO();
	//DAO를 싱글톤 패턴으로 만들고 있음. 객체를 한 번만 생성해서 재사용하도록 함 (메모리 절약 & 일관성 유지)
	
	private ProductDAO() { // 생성자를 `private`으로 막음. 외부에서 `new`로 인스턴스를 만들지 못하게 함
	}
	
	public static ProductDAO getInstance() { //유일하게 외부에서 DAO 객체를 가져올 수 있는 메서드
		return instance; //항상 같은 instance를 반환함
	}
	
	
	//DB 연결 → SQL 작성 → 쿼리 실행 → 결과 받아서 객체에 저장 → 리스트로 반환
	
	//DB에서 전체 목록 가져오기
	public List<ProductVO> selectAllProducts(){
		String sql = "select * from product order by code desc";
		//`product` 테이블의 모든 데이터를 `code` 컬럼 기준으로 **내림차순 정렬**해서 가져오겠다는 의미
		
		List<ProductVO> list = new ArrayList<ProductVO>(); 
		//DB에서 가져온 각 상품 데이터를 담을 ProductVO 객체 리스트, 이걸 나중에 JSP로 넘겨서 화면에 보여줌
		
		//DB 연결에 필요한 객체들을 미리 선언
		Connection conn = null; // `conn`: DB 연결 객체
		PreparedStatement pstmt = null; // `pstmt`: SQL 실행용 객체
		ResultSet rs = null; // `rs`: SELECT 결과 받아오는 객체
		
		try {
			//1. DB연결
			conn = DBManager.getConnection(); // DBManager 클래스에서 DB 연결을 얻어옴
			//2. sql 구문 전송
			pstmt = conn.prepareStatement(sql); 
			//작성한 SQL 구문을 실행하기 위해 `PreparedStatement` 객체 생성
			//DB 서버에 이 SQL을 준비시킴
			
			//3. sql 맵핑
			
			//4. sql 실행
			rs = pstmt.executeQuery(); //SQL을 실행해서 결과셋을 받아옴. SELECT 구문은 항상 executeQuery() 사용 (데이터 반환이 있기 때문)
			
			/*
			 * code number(5) primary key,
				name varchar2(100),
				price number(8),
				pictureurl varchar2(50),
				description varchar2(1000)
			 */
			while(rs.next()) { // 결과셋의 **다음 행이 존재하는 동안 반복**, 한 행 = 상품 하나
				ProductVO mVo = new ProductVO(); //각 행마다 새로운 ProductVO 객체를 생성, 이 객체 하나가 하나의 상품 정보가 됨				
			
				
				//각 컬럼의 값을 읽어서 `ProductVO` 객체에 저장, DB 컬럼명과 Java 필드를 **매핑**함
				mVo.setCode(rs.getInt("code"));
				mVo.setName(rs.getString("name"));
				mVo.setPrice(rs.getInt("price"));
				mVo.setPictureUrl(rs.getString("pictureurl"));
				mVo.setDescription(rs.getString("description"));
				
//				rs.getInt("code");
//				rs.getString("name");
//				rs.getInt("price");
//				rs.getString("pictureurl");
//				rs.getString("description");
				
				list.add(mVo); //완성된 ProductVO 객체를 리스트에 추가, 반복이 끝나면 모든 상품들이 리스트에 들어감
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DBManager.close(conn, pstmt, rs); //DB 연결, Statement, ResultSet을 닫아줌, **리소스 누수 방지** (필수!)
		}	
		
		return list; //모든 상품이 들어있는 리스트를 반환함, 이걸 서블릿에서 받아서 request.setAttribute("productList", list)로 넘김
	} // end electAllProducts()

	public void insertProduct(ProductVO pVo) {
		String sql = "insert into product values(product_seq.nextval, ?,?,?,?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			// 1. 연결
			conn = DBManager.getConnection();
			// 2. sql 구문 전송
			pstmt = conn.prepareStatement(sql);
			// 3. sql 맵핑
			pstmt.setString(1, pVo.getName());
			pstmt.setInt(2, pVo.getPrice());
			pstmt.setString(3, pVo.getPictureUrl());
			pstmt.setString(4, pVo.getDescription());
			
			// 4. 실행
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(conn, pstmt);
		}
		
	} // end insertProduct

	public ProductVO selectProductByCode(String code) {
		
		ProductVO pVo = null;
		
		String sql = "select *from product where code = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 1. 연결
			conn = DBManager.getConnection();
			// 2. sql 구문 전송
			pstmt = conn.prepareStatement(sql);
			// 3. sql 맵핑
			pstmt.setInt(1, Integer.parseInt(code));

			// 4. 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
	            pVo = new ProductVO();
	            pVo.setCode(rs.getInt("code"));
	            pVo.setName(rs.getString("name"));
	            pVo.setPrice(rs.getInt("price"));
	            pVo.setPictureUrl(rs.getString("pictureurl"));
	            pVo.setDescription(rs.getString("description"));
	        }
			
		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }
	    
	    return pVo;
	}//selectOne(String code)

	public void updateProduct(ProductVO pVo) {
		String sql = "update product set name = ?, price = ?, pictureurl = ?, description = ? where code = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			// 1. 연결
			conn = DBManager.getConnection();
			// 2. sql 구문 전송
	        pstmt = conn.prepareStatement(sql);
	        // 3. sql 맵핑
	        pstmt.setString(1, pVo.getName());
	        pstmt.setInt(2, pVo.getPrice());
	        pstmt.setString(3, pVo.getPictureUrl());
	        pstmt.setString(4, pVo.getDescription());
	        pstmt.setInt(5, pVo.getCode());
	        
	        // 4. 실행
	        pstmt.executeUpdate();
	        
		}catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	DBManager.close(conn, pstmt);
	    }
		
	} // end updateProduct

	
}
