package com.saeyan.controller.action;

/*
[1] boardWrite.jsp에서 작성 후 전송 (POST)
   				↓
[2] BoardServlet (Dispatcher 역할)
   				↓
[3] ActionFactory → BoardWriteAction 생성
   				↓
[4] BoardWriteAction: request 데이터 받아 DAO 저장
   				↓
[5] BoardDAO.insertBoard(): DB에 INSERT
   				↓
[6] BoardWriteAction → sendRedirect → board_list 재요청
   				↓
[7] 목록 페이지 다시 출력
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardWriteAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVO bVo = new BoardVO();
		bVo.setName(name);
		bVo.setPass(pass);
		bVo.setEmail(email);
		bVo.setTitle(title);
		bVo.setContent(content);
		
		
		BoardDAO bDao = BoardDAO.getInstance();
		
		bDao.insertBoard(bVo);
		
		//사용자 입력을 받아 BoardVO에 저장
		//DAO를 통해 DB에 저장
		//sendRedirect()를 사용해 다시 목록 페이지로 이동
		
		// 등록 후 → 목록으로 리다이렉트
		//new BoardListAction().execute(request, response);
		response.sendRedirect("BoardServlet?command=board_list");
		//forward가 아닌 redirect를 사용했기 때문에, BoardServlet?command=board_list를 새로 요청하게 됨
		//따라서 목록 페이지 흐름이 다시 동작 (앞서 설명한 BoardListAction)
	}

}
