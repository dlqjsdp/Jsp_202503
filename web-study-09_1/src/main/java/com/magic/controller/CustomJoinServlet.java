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

/*
 [운영자 로그인] 
     ↓
접속: /custom.do (GET)  ← 메뉴에서 "사원등록" 클릭
     ↓
서블릿 doGet() → 세션 검사 (운영자인지 확인)
     ↓
운영자면 → customjoin.jsp로 forward
일반회원 or 비로그인 → login.do로 redirect
     
폼 작성 후 submit (POST)
     ↓
doPost() → 파라미터 수집 → DB insert
     ↓
joinsuccess.jsp로 forward
 */

/* 요약 정리
세션 처리 : session.getAttribute("loginUser")
파라미터 수집 : request.getParameter(...)
SQL 실행 시점 : memberDAO.insertMember(member)
화면 전환 방식 : forward (서버 내부 이동) / redirect (클라이언트 이동)
*/

@WebServlet("/custom.do")
public class CustomJoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	            //doGet() - 운영자 검증 및 페이지 이동
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(); // 세션 처리: 현재 로그인된 사용자를 loginUser 세션에서 가져옴
		EmployeesVO eVo = (EmployeesVO) session.getAttribute("loginUser"); // EmployeesVO로 형변환
		
		if (eVo != null && "A".equals(eVo.getLev())) { //로그인 O && 운영자인 경우만 통과
			//운영자 검증 방식: "A".equals(...) → null 예외 방지한 좋은 방식!
			
			String url = "customjoin.jsp"; // customjoin.jsp에 form이 있음 → 사원등록 화면
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response); // 서버 내부 이동 (forward)
		} else {
			response.sendRedirect("login.do"); //비로그인 또는 일반회원은 로그인 페이지로 보냄
			//클라이언트 이동 (redirect) → URL 변경됨
		}
	}
				//doPost() - 회원 등록 처리
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8"); //POST로 넘어온 한글 데이터를 UTF-8로 처리
		
		EmployeesVO member = new EmployeesVO(); //파라미터를 저장할 DTO 객체 생성
		
		member.setId(request.getParameter("id"));
		member.setPass(request.getParameter("pass"));
		member.setName(request.getParameter("name"));
		member.setLev(request.getParameter("lev"));		
		member.setGender(Integer.parseInt(request.getParameter("gender")));
		member.setPhone(request.getParameter("phone"));
		// ↑ 파라미터 처리 시점
		// 사용자가 form에 입력한 값을 꺼내서 member에 저장
		// request.getParameter("name") → customjoin.jsp에서 name="name"에 대응
		
		EmployeesDAO memberDAO = EmployeesDAO.getInstance();
		memberDAO.insertMember(member); //insertMember() 내부에서 JDBC로 DB에 INSERT 실행됨
		// ↑ 이 부분이 SQL 실행 시점! 
		//(public int insertMember(EmployeesVO eVo)에서)
		//INSERT INTO employees(id, pass, name, lev, gender, phone) ...)
		
		request.setAttribute("member", member);
		request.setAttribute("message", "회원 등록에 성공했습니다.");
		//등록된 회원 정보를 JSP에서 출력 가능하도록 request에 담음
		
		String url = "joinsuccess.jsp";		
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response); 
		//성공 메시지를 담고 JSP 페이지로 이동 (forward)
		//URL은 그대로 유지되고, 서버 내부에서 joinsuccess.jsp 처리됨
		
	}
	

}