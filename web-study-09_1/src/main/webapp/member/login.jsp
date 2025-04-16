<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
td {
	border: 1px solid black;
	text-align: center;
	width: 300px;
}
</style>

</head>
<body>
	<form action="login.do" method="post" name="frm">
		<table align = "center">
			<tr>
				<td colspan="2">로그인</td>
			</tr>
			<tr>
				<td>아이디</td>
				<td><input type="text" name="id" size=10></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="pass" size=10></td>
			</tr>
			<tr>
				<td>레벨</td>
				<td>
					<select name="lev">
						<option value="A">운영자</option>
						<option value="B">일반회원</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="로그인"> 
					<input type="reset" value="취소">
				</td>
			</tr>
			
		</table>
	
	</form>
</body>
</html>