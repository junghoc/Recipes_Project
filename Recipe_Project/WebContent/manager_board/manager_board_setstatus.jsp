<%@page import="dao.ManagerBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	
	// 안드로이드에서 받은 파라미터를 받는다.
	String returns = "";
	String idx = request.getParameter("idx");
	String status = request.getParameter("status");
	
	if(idx != null && status != null){
		ManagerBoardDAO dao = ManagerBoardDAO.getInstance();
		returns = dao.board_status_update(idx, status);
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