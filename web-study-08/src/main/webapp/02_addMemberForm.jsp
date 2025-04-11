<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입하기</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.10.1/jquery.min.js" 
  integrity="sha512-eH6+OZuv+ndnPTxzVfin+li0PXKxbwi1gWWH2xqmljlTfO3JdBlccZkwWd0ZcWAtDTvsqntx1bbVSkWzHUtfQQ==" 
  crossorigin="anonymous" referrerpolicy="no-referrer"></script>
  
<script>
//jQuery를 활용한 이메일 유효성 검사 진행
$(function(){
  $("form").submit(function(){
    var email = $("#email").val();
    var regexp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!regexp.test(email)) {
      alert("올바른 이메일 주소가 아닙니다.");
      $("#email").focus();
      return false;
    }
    return true;
  });
});
</script> 

<style>
*{margin: 0; padding: 0; box-sizing: border-box;}
ul, ol, li{list-style: none; margin: 0; padding: 0;}
a {text-decoration: none; color: #000;}

  body {
    background-color: #FDFAF6;
    color: #333;
    margin: 0;
    padding: 0;
  }

  .wrapper {
    width: 500px;
    margin: 50px auto;
    background-color: #FDFAF6;
    border: 2px solid #26CB4D;
    border-radius: 10px;
    padding: 30px;
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
  }

  h2 {
    text-align: center;
    margin-bottom: 40px;
  }

  table {
    margin: 0 auto;
    border-spacing: 2px;
  }

  td {
    padding: 5px;
  }

  input[type="submit"] {
    margin-top: 20px;
    background-color: #26CB4D;
    border: 1px solid #26CB4D;
    padding: 5px 15px;
    font-weight: bold;
    border-radius: 10px;
    color: #FDFAF6;
  }

</style>
    
</head>
<body>

<div class="wrapper">
	<h2>회원의 정보 입력 폼</h2>
	
	<form method="post" action="02_addMember.jsp">
		<table>
			<tr>
				<td>이름&nbsp;</td>
				<td><input type="text" name="name" size="20"></td>
			</tr>
			<tr>
				<td>아이디&nbsp;</td>
				<td><input type="text" name="userid" size="20"></td>
			</tr>
			<tr>
				<td>비밀번호&nbsp;</td>
				<td><input type="password" name="pwd" size="20"></td>
			</tr>
			<tr>
				<td>이메일&nbsp;</td>
				<td><input type="email" name="email" id="email" size="20"></td>
			</tr>
			<tr>
				<td>전화번호&nbsp;</td>
				<td><input type="text" name="phone" size="20"></td>
			</tr>
			<tr>
				<td>등급&nbsp;</td>
				<td>
					<input type="radio" name="admin" value="1">관리자
					<input type="radio" name="admin" value="0" checked>일반회원
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="전송">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="submit" value="취소">
				</td>
			</tr>
			
		</table>
	</form>
</div>
</body>
</html>