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
	}else{
		BoardDAO dao = BoardDAO.getInstance();
		
		if(type.equals("type_board_like")){
			dao.board_like_update(idx);
			
		}else if(type.equals("type_board_nolike")){
			dao.board_nolike_update(idx);
			
		}
		
		returns = dao.board_like_select(idx);
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