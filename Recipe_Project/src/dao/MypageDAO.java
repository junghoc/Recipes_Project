package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import service.DBService;

public class MypageDAO {

	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static MypageDAO single = null;

	public static MypageDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new MypageDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	//회원탈퇴
	public String user_del_update(String id) {
		System.out.println("id : " + id);
			
		int res = 0;
		String returns = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "update member set DEL_INFO='-1' where id=?";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, id);

			//4.DB로 전송(res:처리된행수)
			res = pstmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			returns = String.format("{res:[{'result','%s'}]}", "fail");
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(res == 1) {
		returns = String.format("{res:[{'result','%s'}]}", "success");
		}
		System.out.println("returns : " + returns);
		return returns;
	}
		
	//회원정보 수정
	public String member_update(String id, String birth, String phone, String email) {
		System.out.println("id : " + id);
		System.out.println("birth : " + birth);
		System.out.println("phone : " + phone);
		System.out.println("email : " + email);
			
		int res = 0;
		String returns = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "update member set birth=?, phone=?, email=? where id=?";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, birth);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);
			pstmt.setString(4, id);

			//4.DB로 전송(res:처리된행수)
			res = pstmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			returns = String.format("{res:[{'result','%s'}]}", "fail");
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(res == 1) {
			returns = String.format("{res:[{'result','%s'}]}", "success");
		}
		System.out.println("returns : " + returns);
		return returns;
	}
	
	public String member_pwd_update(String id, String pwd) {
		System.out.println("id : " + id);
		System.out.println("pwd : " + pwd);

		int res = 0;
		String returns = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "update member set pwd=? where id=?";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, pwd);
			pstmt.setString(2, id);

			//4.DB로 전송(res:처리된행수)
			res = pstmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(res == 1) {
			returns = String.format("{res:[{'result','%s'}]}", "success");
		}
		System.out.println("returns : " + returns);
		return returns;
	}
	
}
