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
		int number = 90;
	
		if(number>=90){
	%>
			<h1>A</h1>
	<%
	 	}else if(number>=80){
	%>
			<h1>B</h1>
	<% 
		}else{
	%>
			<h1>C</h1>
	<%
	 	}
	
	%>
</body>
</html>