<%@page import="dao.QaDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");

 	String sel = request.getParameter("sel");
	String text = request.getParameter("text");
	
	QaDAO dao = QaDAO.getInstance();
	String returns = dao.search_selectList(sel, text);
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