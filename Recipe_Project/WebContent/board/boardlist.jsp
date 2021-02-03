<%@page import="dao.BoardDAO"%>
<%@page import="dao.HomeDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
	request.setCharacterEncoding("utf-8");
	
	BoardDAO dao = BoardDAO.getInstance();
	String returns = dao.board_selectList();
	out.print(returns);
	
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