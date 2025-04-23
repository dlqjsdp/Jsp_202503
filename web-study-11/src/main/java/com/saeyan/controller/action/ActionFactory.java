package com.saeyan.controller.action;

// 어떤 명령어(command)에 어떤 Action을 사용할지 결정
public class ActionFactory {

	private static ActionFactory instance  = new ActionFactory();
	
	private ActionFactory() {
	}
	
	public static ActionFactory getInstance(){
		return instance;
	}
							//=board_view
	public Action getAction(String command) {
		Action action = null;
		
		System.out.println("ActionFactory : " + command);
		
		if(command.equals("board_list")) {
			action = new BoardListAction();  // 여기서 실제 객체 생성
			// "board_list" 명령어면 BoardListAction 객체를 만들어서 반환
		}else if(command.equals("board_write_form")) {
			action = new BoardWriteFormAction();
		}else if(command.equals("board_write")) {
			action = new BoardWriteAction();
			//command=board_write인 경우 BoardWriteAction 객체 생성 및 반환
		}else if(command.equals("board_view")) {
			action = new BoardViewAction();
		}else if(command.equals("board_check_pass_form")) {
			action = new BoardCheckPassFormAction();
		}else if(command.equals("board_check_pass")) {
			action = new BoardCheckPassAction();
		}else if(command.equals("board_delete")) {
			action = new BoardDeleteAction();
		}
				
		return action;
	}
}