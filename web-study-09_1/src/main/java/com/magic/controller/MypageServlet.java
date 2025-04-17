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

/* 전체 흐름 요약
	[클라이언트]
	    ↓
	POST /mypage.do
	    ↓
	[서버 서블릿]
	1. request.getParameter(...) 로 입력값 수집
	2. EmployeesVO에 저장
	3. eDao.updateMember() → UPDATE 실행
	4. eDao.getEmployees() → SELECT 실행
	5. session 갱신
	6. eDao.userCheck() → SELECT 실행
	7. 결과를 session에 저장
	8. joinsuccess.jsp로 포워딩
	    ↓
	[뷰: joinsuccess.jsp]
*/

/* 정리
SQL 실행 시점 : updateMember, getEmployees, userCheck
파라미터 수집 : request.getParameter()
세션 저장/수정 : session.setAttribute()
화면 전환 방식 : RequestDispatcher.forward()
리다이렉트 조건 : 비로그인 상태 (doGet) 
*/

@WebServlet("/mypage.do") //클라이언트가 mypage.do 주소로 요청했을 떄 실행
public class MypageServlet extends HttpServlet { //HttpServlet을 상속하여 서블릭을 동작하게 함
	private static final long serialVersionUID = 1L;
	
       //get방식으로 요청할 경우 호출되는 메서드  //request:클라이언트의 요청 정보를 담고 있음 //response:서버가 클라이언트로 보낼 응답 정보를 담음
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(); //세션 객체 획득 (없으면 생성됨)
		//↑ 세션 처리 시작 지점

		EmployeesVO eVo = (EmployeesVO) session.getAttribute("loginUser");
		//세션에 저장된 로그인 사용자 정보 가져오기, 없으면 로그인하지 않은 상태
		
		if(eVo != null) { //eVo가 null이 아니면 → 로그인한 사용자
			String url="mypage.jsp"; //로그인한 경우 mypage.jsp로 내부 포워딩
			
			//JSP 페이지로 포워딩(위임)할 수 있는 RequestDispatcher 객체를 생성
			//URL에 대한 서버 내부 처리 경로를 준비함
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);//forward는 서버 내부 이동이므로 URL이 바뀌지 않음
			
		}else { 
			response.sendRedirect("login.do"); //브라우저 주소창도 바뀜 (mypage.do → login.do)
			// 비로그인 시 로그인 화면으로 리다이렉트
			// 리다이렉트 = 클라이언트에 새로운 요청을 보내도록 함
		}
		
	}
			//POST 방식 요청이 들어올 때 호출, <form method="post">로 요청이 들어온 경우
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8"); //한글 인코딩 문제를 방지하기 위함
//		System.out.println(request.getParameter("name")); //콘솔에 name 파라미터(사용자 이름)를 출력하여 디버깅 용도로 사용
		HttpSession session = request.getSession(); //세션 객체 재사용, 로그인 정보나 상태를 저장하는 데 사용
		EmployeesVO member = new EmployeesVO(); //수정할 회원 정보를 담을 VO 객체 생성
		
		//폼(form)에서 넘어온 각각의 파라미터 값을 꺼내서 EmployeesVO객체에 저장
		member.setId(request.getParameter("id"));
		member.setPass(request.getParameter("pass"));
		member.setName(request.getParameter("name"));
		member.setLev(request.getParameter("lev"));
		
		//gender 값이 있을 경우 정수로 변환하여 저장
		if(request.getParameter("gender")!=null)
			member.setGender(Integer.parseInt(request.getParameter("gender").trim()));
		
		member.setPhone(request.getParameter("phone")); //전화번호 값 저장
		
		EmployeesDAO eDao = EmployeesDAO.getInstance(); //DAO 객체 가져오기 (싱글톤 패턴), DB 작업은 이 DAO 객체를 통해 수행함
		eDao.updateMember(member); // ← SQL 실행 시점 1 (UPDATE 문 실행), 사용자 정보 DB 업데이트 수행
		EmployeesVO eVo = eDao.getEmployees(member.getId()); // ← SQL 실행 시점 2 (SELECT 문 실행), 수정된 정보 다시 DB에서 가져오기 (정확한 최신 상태 반영)
		
		request.setAttribute("member", eVo);
		request.setAttribute("message", "회원 정보가 수정되었습니다.");
		//JSP에서 사용할 정보를 request 범위에 저장
		
		session.setAttribute("loginUser", eVo); // 세션 갱신, 최신 사용자 정보를 세션에 반영 (중요!)
		
		
//		System.out.println(eVo);
		

		int result = eDao.userCheck(member.getId(), member.getPass(), member.getLev());
		// ↑ SQL 실행 시점 3, 로그인 타입 재확인 (운영자 or 일반회원)
		session.setAttribute("result", result); // 그 결과를 세션에 저장, 이후 JSP에서 ${result}로 접근 가능

		
		String url="joinsuccess.jsp"; //수정 완료 후 joinsuccess.jsp 페이지로 이동
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	
	}

}