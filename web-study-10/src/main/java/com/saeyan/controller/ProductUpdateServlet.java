package com.saeyan.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.saeyan.dao.ProductDAO;
import com.saeyan.dto.ProductVO;

/*
지금 이 코드는 상품 수정 기능을 완성한 ProductUpdateServlet
GET 방식으로 상품 수정 폼을 보여주고,
POST 방식으로 수정된 데이터를 받아서 DB에 저장하는 흐름
 */

/* 전체 흐름 요약
	[사용자] ─ GET /productUpdate.do?code=3
	   		↓
	[서블릿 doGet()]
	   → ProductDAO.selectProductByCode("3")
	   → request.setAttribute("product", pVo)
	   → forward to productUpdate.jsp
	
	[사용자 입력 후 제출]
	   		↓
	[서블릿 doPost()]
	   → MultipartRequest로 값 추출
	   → ProductVO에 저장
	   → ProductDAO.updateProduct(pVo)
	   → redirect to productList.do
 */


@WebServlet("/productUpdate.do")
public class ProductUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//doGet() 메서드 흐름 (수정할 상품 보여주기)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String code = request.getParameter("code");
		//URL 쿼리스트링으로 넘겨받은 상품 번호(code)를 받음. ex)`productUpdate.do?code=3`
		
		//ProductDAO pDao = ProductDAO.getInstance();
		//ProductVO pVo = pDao.getselectOne(code);
		
		ProductVO pVo = ProductDAO.getInstance().selectProductByCode(code);
		//DAO를 통해 해당 상품 코드에 해당하는 상품 정보를 조회
		//이 정보는 화면에서 수정 폼에 미리 채워 넣기 위해 사용됨
		
		request.setAttribute("product", pVo); 
		//조회한 상품 정보를 `request`에 담아서 JSP로 전달
		//key 이름은 `"product"`
		
		request.getRequestDispatcher("product/productUpdate.jsp")
			.forward(request, response); //수정 폼 JSP로 forward 이동
		//URL은 유지되고 서버 내부 이동 → 데이터 전달 가능
		
	
	}

	//doPost() 메서드 흐름 (수정 처리)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1) 한글 처리
		request.setCharacterEncoding("utf-8"); //POST 방식에서 한글이 깨지지 않도록 설정
		
		// 2) 파일 업로드 처리 설정
		ServletContext context = getServletContext();
		String path = context.getRealPath("upload");
		String encType = "utf-8";
		int sizeLimit = 20*1024*1024;
		/* 	 변수 : 설명
			path : 실제 서버 경로 (/upload 폴더 실제 위치)
			encType : 업로드 인코딩 방식
			sizeLimit : 업로드 최대 크기 (20MB)
		 */
		
		// 3) 파일 처리
		MultipartRequest multi = new MultipartRequest(request, path, sizeLimit, 
				encType, new DefaultFileRenamePolicy()); 
		//enctype="multipart/form-data"로 넘어온 폼 데이터를 파싱함
		//이미지 파일 + 텍스트 데이터를 동시에 받을 수 있게 해줌
		
		//System.out.println(path);
		
		// 4) 파라미터 추출
		int code = Integer.parseInt(request.getParameter("code"));
		String name = multi.getParameter("name");
		int price = Integer.parseInt(multi.getParameter("price"));
		String pictureUrl = multi.getFilesystemName("pictureUrl");
		String description = multi.getParameter("description");
		//입력 폼에서 전달된 상품 데이터들을 추출, 숫자는 parseInt()로 형변환
		
		// 5) 이미지 처리 (파일 미변경 시 기존 이미지 유지)
		// 수정 시 이미지 첨부를 하지 않으면, 기본 이미지 사용(noimage.gif)
		if(pictureUrl == null) {
			pictureUrl = multi.getParameter("nonmakeImg");
		}
		
		// 6) ProductVO에 담기
		ProductVO pVo = new ProductVO();
		
		pVo.setCode(code);
		pVo.setName(name);
		pVo.setPrice(price);
		pVo.setPictureUrl(pictureUrl);
		pVo.setDescription(description);
		//수정된 내용을 VO 객체에 저장, 이 VO를 DAO로 넘겨서 DB를 업데이트
		
		// 7) DAO 호출
		ProductDAO.getInstance().updateProduct(pVo);
		//싱글톤 DAO 인스턴스 생성, updateProduct() 호출하여 DB에 수정 반영
	
		// 8) 리스트 페이지로 리다이렉트
		response.sendRedirect("productList.do");
		//수정이 끝나면 다시 상품 목록으로 이동, 리다이렉트이므로 URL이 바뀌고 새 요청 발생
		
	}

}
