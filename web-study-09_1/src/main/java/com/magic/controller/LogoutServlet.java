package com.magic.controller;

import java.io.IOException;

/* 흐름도 요약
	사용자가 logout.do 요청 → 이 서블릿이 실행
	세션 객체 가져오기
	세션 무효화 → 로그인 정보 삭제
	로그인 페이지(login.jsp)로 포워딩
*/

/*SQL 실행 없음
	로그아웃은 단순히 세션만 종료하고,
	데이터베이스와는 아무런 연동이 없습니다.
*/

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout.do") //클라이언트가 logout.do로 요청을 보낼 경우 이 서블릿이 실행
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(); //현재 브라우저(클라이언트)의 세션 객체를 가져옴, 로그인 상태의 사용자 세션도 여기서 관리
		session.invalidate(); //세션 무효화, 현재 사용자에 대한 모든 세션 데이터를 제거, 완전 초기화
		// ↑ 이 시점에서 session.getAttribute("loginUser"), session.getAttribute("result") 등은 모두 삭제
		
		//로그아웃이 완료되었으므로 로그인 페이지로 다시 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response); // 브라우저 URL은 바뀌지 않고 내부에서 이동 (forward 방식).
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response); //POST 요청이 들어와도 동일한 처리를 하도록 doGet()을 호출
	}

}
