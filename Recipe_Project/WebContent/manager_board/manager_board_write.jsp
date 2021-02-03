<%@page import="dao.ManagerBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");

	String title = request.getParameter("title");
	String content = request.getParameter("content");
	String result = "{res:[{'result':'no'}]}";
	
	if(title == null || content == null){
		out.print(result);
		return;
	}
	
	ManagerBoardDAO dao = ManagerBoardDAO.getInstance();
	
	result = dao.insert_board(title, content);
	out.print(result);
	
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