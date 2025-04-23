package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.controller.action.Action;
import com.saeyan.controller.action.ActionFactory;

//모든 요청을 먼저 받는 중앙 컨트롤러 역할

//Spring --> DispatcherSevlet(Front Controller)
@WebServlet("/BoardServlet")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//BoardServlet?command=board_write
		String command = request.getParameter("command"); // "board_write" // board_view
		// 파라미터 command=board_list를 추출
		// boardWrite.jsp에서 사용자가 입력한 데이터가 BoardServlet?command=board_write로 전송됨 -> "board_write"
		System.out.println("BoardServlet에서 요청을 받음을 확인 : " + command);
		
		ActionFactory af = ActionFactory.getInstance(); 
		// ActionFactory 싱글톤 객체 생성 (또는 가져오기) -  한 번만 생성해서 계속 재사용 (싱글톤 패턴)
		// ActionFactory는 요청에 따라 적절한 Action을 생성해주는 중앙 Action 매핑 클래스
		Action action = af.getAction(command);  // BoardWriteAction 생성 // BoardViewAction 생성
		// 사용자 요청(command)에 해당하는 Action 객체를 생성하거나 반환
		// command = "board_list"라면 → new BoardListAction() 객체 생성해서 반환
		
		if(action != null) {
			action.execute(request, response); // 실행
			// 반환된 Action 객체의 execute() 실행, 이게 핵심 실행 부분
			// 실제 로직은 각 Action 구현체 내부에서 처리함 (예: DAO 호출 → request에 데이터 담기 → JSP로 forward 등)
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doGet(request, response);
	}

}
