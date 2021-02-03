<%@page import="dao.ManagerBoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	request.setCharacterEncoding("utf-8");

	String search = request.getParameter("search");
	
	ManagerBoardDAO dao = ManagerBoardDAO.getInstance();
	
	String returns = "";
	
	if(search != null){
		returns = dao.selectList_search(search);
	}else{
		returns = dao.selectList();
	}
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