<%@page import="com.magic.dao.EmployeesDAO"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	EmployeesDAO eDao = EmployeesDAO.getInstance();

	Connection conn = eDao.getConnection();
	out.println(conn + "<br>");
	out.println("DB 연결 성공");
	
%>

</body>
</html>