<%@page import="dao.ManagerMemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	
	// 안드로이드에서 받은 파라미터를 받는다.
	String returns = "";
	String idx = request.getParameter("seq_member_idx");
	
	if(idx != null){
		ManagerMemberDAO dao = ManagerMemberDAO.getInstance();
		returns = dao.selectOne(idx);
		out.print(returns);
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