package com.saeyan.controller.action;

/*
[1] boardView.jsp에서 "수정" 버튼 클릭  
   → open_win('BoardServlet?command=board_check_pass_form&num=5', 'update')  
    ↓  
[2] BoardServlet → ActionFactory → BoardCheckPassFormAction 실행  
    ↓  
[3] boardCheckPass.jsp 비밀번호 입력 화면 표시  
    ↓  
[4] 사용자가 비밀번호 입력 후 submit  
   → BoardServlet?command=board_check_pass&num=5&pass=입력값  
    ↓  
[5] BoardServlet → ActionFactory → BoardCheckPassAction 실행  
    - DAO에서 글 번호로 해당 글 정보 조회  
    - 입력한 비밀번호와 DB 비밀번호 비교  
       → 일치하면 다음 단계로 진행  
    ↓  
[6] BoardServlet?command=board_update_form&num=5 로 리다이렉트  
    ↓  
[7] BoardServlet → ActionFactory → BoardUpdateFormAction 실행  
    - num으로 기존 글 정보 DB 조회  
    - request.setAttribute("board", bVo)  
    ↓  
[8] boardUpdate.jsp 로 forward  
    - 기존 제목, 내용, 이메일 등이 <input> value에 미리 채워져 있음  
    ↓  
[9] 사용자가 수정한 후 form submit (POST)  
   → BoardServlet?command=board_update  
    ↓  
[10] BoardServlet → ActionFactory → BoardUpdateAction 실행  
     - request에서 수정된 값 받기  
     - BoardVO 객체 생성 및 저장  
     - BoardDAO.updateBoard(bVo) 실행 (DB 반영)  
    ↓  
[11] sendRedirect → BoardServlet?command=board_view&num=5  
    ↓  
[12] BoardServlet → ActionFactory → BoardViewAction 실행  
    - 해당 글 DB에서 다시 조회  
    - 조회수 증가 포함  
    ↓  
[13] boardView.jsp forward → 수정된 내용 화면 출력  
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardUpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 요청 파라미터 꺼내기 (사용자가 입력한 값 가져오기)
		String num = request.getParameter("num");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVO bVo = new BoardVO();
		bVo.setNum(Integer.parseInt(num));
		bVo.setName(name);
		bVo.setPass(pass);
		bVo.setEmail(email);
		bVo.setTitle(title);
		bVo.setContent(content);
		
//		BoardDAO bDao = BoardDAO.getInstance();
//		bDao.updateBoard(bVo);
		
		BoardDAO.getInstance().updateBoard(bVo);
		
		//리스트 목록 이동
		new BoardListAction().execute(request, response);
		
	}

}
