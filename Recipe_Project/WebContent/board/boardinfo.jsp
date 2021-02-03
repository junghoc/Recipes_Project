<%@page import="dao.BoardDAO"%>
<%@page import="dao.HomeDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	
	// 안드로이드에서 받은 파라미터를 받는다.
	String returns = "";
	String type = request.getParameter("type");
	String idx = request.getParameter("idx");
	
	if(type==null){
		return;
	}else if(type.equals("type_board")){
		BoardDAO dao = BoardDAO.getInstance();
		returns = dao.board_selectone(idx);
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