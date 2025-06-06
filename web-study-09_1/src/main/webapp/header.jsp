<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
table.menu td {
	border: 1px solid black;
	text-align: center;
	width: 200px;
	height: 60px; 
}

table.menu td.hover:hover {
	background-color: #FFD400;
}
</style>

</head>
<body>

	<table class="menu" align = "center" style="width:90%">
		<c:if test="${empty loginUser}">
			<tr>
				<td></td>
				<td></td>
				<td>로그인</td>
				<td>
					사원등록<br>
					<span style="color: gray;">(운영자로 로그인 후 사용 가능)</span>
				</td>
				<td>
					마이페이지<br>
					<span style="color: gray;">(로그인 후 사용 가능)</span>
				</td>
			</tr>
		</c:if>
		
		<c:if test="${!empty loginUser}">
			<tr>
				<td>${loginUser.name}님 반갑습니다.</td>
				<td>레벨 : ${loginUser.lev}</td>
				<td><a href="logout.do">로그아웃</a></td> 
				<c:choose>
					<c:when test="${result==2}">
						<td class="hover"><a href="custom.do">사원 등록</a></td>
					</c:when>
					<c:when test="${result==3}">
						<td>사원 등록<br>(운영자만 가능)</td>
					</c:when>
				</c:choose>
				<td class="hover">
					<a href="mypage.do">마이페이지</a>
				</td>
			</tr>
		
		</c:if>
	</table>

</body>
</html>