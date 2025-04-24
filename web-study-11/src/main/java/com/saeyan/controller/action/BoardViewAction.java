package com.saeyan.controller.action;

/*
[1] boardList.jsp에서 제목 클릭 (GET, num 포함)
    ↓
[2] BoardServlet (Dispatcher 역할) 요청 수신
    ↓
[3] ActionFactory → BoardViewAction 생성
    ↓
[4] BoardViewAction:
       - request.getParameter("num")로 글 번호 받기
       - BoardDAO.updateReadCount(num) → 조회수 증가
       - BoardDAO.selectOneBoardByNum(num) → 해당 글 정보 가져오기
       - request.setAttribute("board", bVo)
    ↓
[5] forward → boardView.jsp
    ↓
[6] boardView.jsp: ${board.title}, ${board.content} 등으로 데이터 출력
    ↓
[7] 사용자 화면에 해당 글 상세 내용 렌더링됨
 */

/*
사용자 클릭
   ↓
BoardServlet (command=board_view)
   ↓
BoardViewAction
   ↓ ① 조회수 증가
   ↓ ② num에 해당하는 데이터 조회
   ↓ ③ request.setAttribute("board", bVo)
   ↓
forward → boardView.jsp
   ↓
HTML 렌더링 → 사용자에게 출력 
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardViewAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int num = Integer.parseInt(request.getParameter("num"));
		// 파라미터로 받은 글 번호를 int num으로 형변환
		
		BoardDAO bDao = BoardDAO.getInstance();
		// DAO 객체 준비
		
		//조회수 증가
		bDao.updateReadCount(num);
		//글을 볼 때마다 조회수(readcount)를 1 증가시킴
		//sql 구문 : UPDATE board SET readcount = readcount + 1 WHERE num = ?
		
		//num(primary key)해당하는 데이터 가져오기 -> 해당 글 번호로 DB 조회
		BoardVO bVo = bDao.selectOneBoardByNum(num);
		//sql 구문 : SELECT * FROM board WHERE num = ?
		//해당 글 번호에 맞는 BoardVO 객체를 생성해서 반환
		request.setAttribute("board", bVo); //request 객체에 데이터 저장
		//JSP에서 사용할 수 있도록 board라는 이름으로 저장함
		
		request.getRequestDispatcher("/board/boardView.jsp")
			.forward(request, response);
		//forward로 화면 이동 (브라우저 주소는 그대로 유지됨)
		//boardView.jsp에서 ${board.title} 등으로 출력 가능
	}

}
