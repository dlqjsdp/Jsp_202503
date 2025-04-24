package com.saeyan.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardUpdateFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String num = request.getParameter("num");
		
		// DB에서 기존 글 정보 조회
		BoardDAO bDao = BoardDAO.getInstance();
		BoardVO bVo = bDao.selectOneBoardByNum(Integer.parseInt(num));
		
		// JSP에 데이터 전달
		request.setAttribute("board", bVo);
		
		// 수정폼으로 이동
		request.getRequestDispatcher("/board/boardUpdate.jsp")
			.forward(request, response);
	}

}
