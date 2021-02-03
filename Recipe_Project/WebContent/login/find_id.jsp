<%@page import="vo.MemberVO"%>
<%@page import="dao.LoginDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	
	// 안드로이드에서 받은 파라미터를 받는다.
	String returns = "";
	String type = request.getParameter("type");
	String phone = request.getParameter("phone");
	String name = request.getParameter("name");
	
	if(type==null){
		return;
	}else if(type.equals("type_findid")){
		LoginDAO login  = LoginDAO.getInstance();
		returns = login.find_id(phone,name);
		out.print(returns); // 성공 실패 여부를 안드로이드로 재 전송
		
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
</body>
</html>