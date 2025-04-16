package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.MemberDAO;
import com.saeyan.dto.MemberVO;

@WebServlet("/memberUpdate.do")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userid = request.getParameter("userid");
		
		MemberDAO mDao = MemberDAO.getInstance();
		MemberVO mVo = mDao.getMember(userid);
		
		request.setAttribute("mVo", mVo);
		
		request.getRequestDispatcher("member/memberUpdate.jsp")
			.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("utf-8");
		
		String name = request.getParameter("name");
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String admin = request.getParameter("admin");
		
		MemberVO mVo = new MemberVO();
		mVo.setName(name);
		mVo.setUserid(userid); // WHERE userid=? 조건이 필요하므로 userid를 넣어야 함.
		// userid를 넣지 않으면 mVo.getUserid() 값이 null이므로 조건이 항상 false 
		// → 업데이트가 실행되지 않음!
		mVo.setPwd(pwd);
		mVo.setEmail(email);
		mVo.setPhone(phone);
		mVo.setAdmin(Integer.parseInt(admin));
		
		MemberDAO mDao = MemberDAO.getInstance();
		
		mDao.updateMember(mVo); // 반환타입이 없어 result 생략
		//int result = mDao.updateMember(mVo);
		
		response.sendRedirect("login.do");
	}

}