<%@page import="dao.MypageDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");

	//안드로이드에서 넘겨준 파라비터를 받는다
	String returns = "";
	String type = request.getParameter("type");
	String id = request.getParameter("id");
	
	
	if(type == null){
		return;
	}else if (type.equals("type_withdrawal")){
		
		MypageDAO mypage = MypageDAO.getInstance();
		returns = mypage.user_del_update(id);
		out.print(returns);//성공 실패여부를 안드로이드로 재 전송
		
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