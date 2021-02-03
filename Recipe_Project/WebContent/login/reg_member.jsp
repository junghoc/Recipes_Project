<%@page import="vo.MemberVO"%>
<%@page import="dao.LoginDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	
	// 안드로이드에서 받은 파라미터를 받는다.
	String returns = "";
	
	String type = request.getParameter("type");
	String id = request.getParameter("id");
	String pwd = request.getParameter("pwd");
	String name = request.getParameter("name");
	String gender = request.getParameter("gender");
	String birth = request.getParameter("birth");
	String phone = request.getParameter("phone");
	String email = request.getParameter("email");
		
	// vo에 저장
	MemberVO vo = new MemberVO();
	vo.setId(id);
	vo.setPwd(pwd);
	vo.setName(name);
	vo.setGender(gender);
	vo.setBirth(birth);
	vo.setPhone(phone);
	vo.setEmail(email);
	
	if(type==null){
		return;
	}else if(type.equals("type_register")){
		LoginDAO login  = LoginDAO.getInstance();
		returns = login.reg_member(vo);
		out.print(returns); // 성공 실패 여부를 안드로이드로 재 전송
		
		// {'res':'%s'}} 가 오게 된다.
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