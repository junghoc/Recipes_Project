<%@page import="dao.MypageDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");

	//안드로이드에서 넘겨준 파라비터를 받는다
	String returns = "";
	String type = request.getParameter("type");
	
	if(type == null){
		return;
	}else if (type.equals("type_mypage_user")){
		//파라미터 받기
		String id = request.getParameter("id");
		String birth = request.getParameter("birth");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		
		MypageDAO mypage = MypageDAO.getInstance();
		returns = mypage.member_update(id, birth, phone, email);
		out.print(returns);//성공 실패여부를 안드로이드로 재 전송
		
	}else if (type.equals("type_mypage_user")){
		//파라미터 받기
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MypageDAO mypage = MypageDAO.getInstance();
		returns = mypage.member_pwd_update(id, pwd);
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