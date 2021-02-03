package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import service.DBService;
import vo.BoardVO;
import vo.MemberVO;

public class ManagerMemberDAO {

	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static ManagerMemberDAO single = null;

	public static ManagerMemberDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new ManagerMemberDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	public String selectList() {

		List<MemberVO> list = new ArrayList<MemberVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM MEMBER";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberVO vo = new MemberVO();
				//현재레코드값=>Vo저장			
				vo.setMember_idx(rs.getInt("MEMBER_IDX"));
				vo.setId(rs.getString("ID"));
				vo.setPwd(rs.getString("PWD"));
				vo.setName(rs.getString("NAME"));
				vo.setGender(rs.getString("GENDER"));
				vo.setBirth(rs.getString("BIRTH"));
				vo.setPhone(rs.getString("PHONE"));
				vo.setEmail(rs.getString("EMAIL"));
				vo.setReg_date(rs.getString("REG_DATE"));
				vo.setMgr(rs.getInt("MGR"));
				vo.setDel_info(rs.getInt("DEL_INFO"));			
			
				//ArrayList추가
				list.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//list를 JSON으로 변환
		JsonArray jsonArray = new JsonArray();
				
		for (int i = 0; i < list.size(); i++) {
			MemberVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("member_idx", vo.getMember_idx());
			jsonObject.addProperty("id", vo.getId());
			jsonObject.addProperty("pwd", vo.getPwd());
			jsonObject.addProperty("name", vo.getName());
			jsonObject.addProperty("gender", vo.getGender());
			jsonObject.addProperty("birth", vo.getBirth());
			jsonObject.addProperty("phone", vo.getPhone());
			jsonObject.addProperty("email", vo.getEmail());
			jsonObject.addProperty("reg_date", vo.getReg_date());
			jsonObject.addProperty("mgr", vo.getMgr());
			jsonObject.addProperty("del_info", vo.getDel_info());
								
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			jsonObject.addProperty("birth",format.format(vo.getBirth()));
//			jsonObject.addProperty("reg_date",format.format(vo.getReg_date()));
					
			
//			if(vo.getDel_info() == 0) {
//				jsonObject.addProperty("del_info","normal");
//			}else {
//				jsonObject.addProperty("del_info","deleted");
//			}
			
			jsonArray.add(jsonObject);
		}
		
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
	public String selectOne(String idx) {

		MemberVO vo = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM MEMBER WHERE MEMBER_IDX=?";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 설정
			pstmt.setString(1, idx);

			//4.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo = new MemberVO();
				//현재레코드값=>Vo저장
				vo.setMember_idx(rs.getInt("member_idx"));
				vo.setId(rs.getString("ID"));
				vo.setPwd(rs.getString("PWD"));
				vo.setName(rs.getString("NAME"));
				vo.setGender(rs.getString("GENDER"));
				vo.setBirth(rs.getString("BIRTH"));
				vo.setPhone(rs.getString("PHONE"));
				vo.setEmail(rs.getString("EMAIL"));
				vo.setReg_date(rs.getString("REG_DATE"));
				vo.setMgr(rs.getInt("MGR"));
				vo.setDel_info(rs.getInt("DEL_INFO"));
				System.out.println("idx : " + rs.getInt("member_idx"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("member_idx", vo.getMember_idx());
		jsonObject.addProperty("id", vo.getId());
		jsonObject.addProperty("pwd", vo.getPwd());
		jsonObject.addProperty("name", vo.getName());
		jsonObject.addProperty("gender", vo.getGender());
		jsonObject.addProperty("birth", vo.getBirth());
		jsonObject.addProperty("phone", vo.getPhone());
		jsonObject.addProperty("email", vo.getEmail());
		jsonObject.addProperty("reg_date", vo.getReg_date());
		jsonObject.addProperty("mgr", vo.getMgr());
		jsonObject.addProperty("del_info", vo.getDel_info());
		String del_info = "NORMAL";
		if (vo.getDel_info() != 0) {
			del_info = "DELETED";
		}
		jsonObject.addProperty("del_info", del_info);
		
		jsonArray.add(jsonObject);
		
		return "{res:" + jsonArray.toString() + "}";
	}
}
