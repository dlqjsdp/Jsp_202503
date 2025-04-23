package com.saeyan.controller.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardListAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/board/boardList.jsp";
		
		// ① DAO 호출
		// BoardDAO를 통해 게시글 전체 데이터를 조회 (selectAllBoards())
		BoardDAO bDao = BoardDAO.getInstance();
		List<BoardVO> boardList = bDao.selectAllBoards();
		
		// ② request 객체에 데이터 저장
		// JSP에서 사용할 수 있도록 request.setAttribute("boardList", list)
		request.setAttribute("boardList", boardList);
		
		// ③ View 연결
		// 결과를 /board/boardList.jsp로 forward (내부 이동) → 브라우저 주소는 그대로지만, 화면 내용은 JSP로 바뀜
		// forward -> boardList.jsp 이동
		request.getRequestDispatcher(url)
			.forward(request, response);
	}

}