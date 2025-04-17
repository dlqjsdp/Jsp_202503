package com.magic.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.magic.dao.EmployeesDAO;
import com.magic.dto.EmployeesVO;

/* 전체 구조 요약: LoginServlet
	URL 패턴: /login.do
	처리 방식: GET, POST
	연동 DB: EMPLOYEES 테이블
	사용 객체:EmployeesDAO,HttpSession,EmployeesVO 
*/

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//요청을 get방식으로 받음.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = "login.jsp"; //최초 요청 시 기본 이동할 페이지는 로그인 화면
		
		HttpSession session = request.getSession(); //브라우저에 연결된 세션 가져오기 (없으면 새로 생성)
		
		if (session.getAttribute("loginUser") != null) {// 이미 로그인한 사용자라면 아래 실행
			
			//DB에서 최신 정보로 로그인 사용자 갱신
			EmployeesVO eVo = (EmployeesVO)session.getAttribute("loginUser");
			EmployeesDAO eDao = EmployeesDAO.getInstance();
			eVo = eDao.getEmployees(eVo.getId()); 
			// ↑ SQL 실행 시점! 
			//(public EmployeesVO getEmployees(String id)에서 
			//SELECT * FROM EMPLOYEES WHERE id = ? 실행)
			
			session.setAttribute("loginUser", eVo); //세션 갱신
			url = "main.jsp"; // 메인으로 이동
		}
		
		// 로그인 or 메인 페이지로 내부 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}


/*
 <흐름도: doPost() 기준 로그인 흐름 요약>
	[브라우저 로그인 form 제출] 
		    ↓
	[doPost()] ← POST 방식
		    ↓
	request.getParameter("id", "pass", "lev") ← 클라이언트 파라미터 받기
		    ↓
	EmployeesDAO.userCheck(id, pass, lev) ← DB 검사
		    ↓
	if 로그인 성공 (2: 운영자, 3: 일반회원)
		    ↓
	세션에 로그인 정보 저장 (session.setAttribute)
		    ↓
	메인 페이지로 이동
	else
		   	↓
	실패 메시지 저장 후 로그인 페이지로 이동
*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//사용자가 form에서 입력한 파라미터 수신
		//클라이언트 → 서버로 전달되는 값 처리 (parameter)
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String lev = request.getParameter("lev");
		
		String url = "login.jsp";
		
		//DB연결해서 id, pass 해당하는 자가 있는지 확인!		
		EmployeesDAO eDao = EmployeesDAO.getInstance();
		int result = eDao.userCheck(id, pass, lev);
		//↑ SQL 실행 시점! 
		//(public int userCheck(String id, String pass, String lev)에서 
		//SELECT pass, lev FROM EMPLOYEES WHERE id = ?)
		//userCheck()에서 DB 조회 후 결과 반환
		/*
		 	결과값 의미:
			2: 운영자 로그인 성공
			3: 일반회원 로그인 성공
			1: 권한 불일치
			0: 비밀번호 틀림
			-1: 아이디 없음 		  
		 */
		
		//System.out.println("로그인 여부 : " + result);
		
		if(result >= 2) { //로그인 성공 분기
			EmployeesVO eVo = eDao.getEmployees(id); // ← SQL 실행 시점!
			//(public EmployeesVO getEmployees(String id)에서 
			//SELECT * FROM EMPLOYEES WHERE id = ? 실행)
			
			//로그인 성공 시 세션에 사용자 정보 저장
			HttpSession session = request.getSession(); 
			session.setAttribute("loginUser", eVo); //loginUser: 로그인된 사용자 정보
			session.setAttribute("result", result); //result: 권한 구분값 (2 or 3)

			if (result == 2) {
				request.setAttribute("message", "운영자로 로그인하셨습니다.");
			} else {
				request.setAttribute("message", "회원님 환영합니다 :)");
			} //사용자 구분에 따라 메시지 설정
			
			request.setAttribute("result", result);

			url = "main.jsp"; //로그인 성공 후 메인 페이지 이동

		} else if(result == 0) {
			request.setAttribute("message", "비밀번호가 맞지 않습니다.");

		} else if(result == -1) {
			request.setAttribute("message", "존재하지 않는 회원입니다.");

		} else {
			request.setAttribute("message", "권한이 없습니다.");
		}//로그인 실패 시 메시지 설정
		
		request.getRequestDispatcher(url).forward(request, response); //최종적으로 설정된 JSP로 포워딩
	}

}
