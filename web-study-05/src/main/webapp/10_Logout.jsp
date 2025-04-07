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
		session.invalidate(); //세션 삭제
	%>
	
	<script type="text/javascript"> //로그아웃 되었다는 알림창 뜸
		alert("로그아웃 되었습니다.")
		location.href="10_loginForm.jsp"
	</script>
</body>
</html>