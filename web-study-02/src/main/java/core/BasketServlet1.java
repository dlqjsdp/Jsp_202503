package core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BasketServlet1")
public class BasketServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		
		String productid = request.getParameter("productid");
		String productName = request.getParameter("productName");
		String imgSrc = request.getParameter("imgSrc");
		
		PrintWriter out = response.getWriter();
		out.print("<html><body>");
		out.println("<h1>선택한 상품 : " + productid + "</h1>");
		out.println(imgSrc);

		out.println("<hr>");
		
		out.println("<br><a href='javascript:history.go(-1)'>상품 선택 화면</a>");
		out.print("</body></html>");
		out.close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
