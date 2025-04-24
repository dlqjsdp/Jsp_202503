package com.saeyan.controller.action;

/*
[1] boardView.jsp에서 "삭제" 클릭
    ↓
[2] BoardServlet?command=board_check_pass_form&num=5
    ↓
[3] 비밀번호 입력 화면(boardCheckPass.jsp) 표시
    ↓
[4] 입력값 전송 → board_check_pass 처리
    ↓
[5] 비밀번호 일치 시 → boardDelete.jsp forward
    ↓
[6] 사용자 "삭제 확인" 클릭
    ↓
[7] BoardDeleteAction 실행 → 글 삭제
    ↓
[8] sendRedirect → board_list 재요청
    ↓
[9] 목록 화면 다시 출력
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;

public class BoardDeleteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String num = request.getParameter("num");
		
		BoardDAO bDao = BoardDAO.getInstance();
		bDao.deleteBoard(Integer.parseInt(num));
		
		response.sendRedirect("BoardServlet?command=board_list");
		
//		new BoardListAction().execute(request, response);
	}

}
