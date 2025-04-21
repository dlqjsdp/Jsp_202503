package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.ProductDAO;
import com.saeyan.dto.ProductVO;


@WebServlet("/productDelete.do")
public class ProductDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String code = request.getParameter("code");
		
		ProductVO pVo = ProductDAO.getInstance().selectProductByCode(code);
		
		request.setAttribute("product", pVo);
		
		request.getRequestDispatcher("product/productDelete.jsp")
			.forward(request, response);
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		int code = Integer.parseInt(request.getParameter("code"));
//		System.out.println("code : " + code);
		
		//삭제 처리
		ProductDAO.getInstance().deleteProductByCode(code);
		
		response.sendRedirect("productList.do");
	}

}
