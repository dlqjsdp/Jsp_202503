<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty loginUser}">
	<%-- <jsp:forward page="login.do"></jsp:forward> --%>
	
	<script type="text/javascript">
		window.location.href="login.do";
	</script>
	
</c:if> 

<%@ include file="header.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 관리</title>
</head>
<body>
	<h2 style="text-align: center;">회원 전용 페이지</h2>
	<form action="logout.do">
		<div style="text-align: center;">
		<img src="https://pimg.mk.co.kr/meet/2014/02/image_listmain_2014_269010_1392801484.jpg" width="250px" height="300px">
	</div>
	</form>

</body>
</html>