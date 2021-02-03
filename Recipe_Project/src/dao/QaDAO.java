package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import service.DBService;
import vo.QaVO;

public class QaDAO {

	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static QaDAO single = null;

	public static QaDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new QaDAO();
		//생성된 객체정보를 반환
		return single;
	}
		
	//QA 기본 리스트 출력
	public String selectList() {

		List<QaVO> list = new ArrayList<QaVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM qa";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				QaVO vo = new QaVO();
				//현재레코드값=>Vo저장
				vo.setQa_idx(rs.getInt("qa_idx"));
				vo.setQa_title(rs.getString("qa_title"));
				vo.setQa_id(rs.getString("qa_id"));
				vo.setQa_view(rs.getInt("qa_view"));
				vo.setQa_date(rs.getDate("qa_date"));
				vo.setQa_state(rs.getString("qa_state"));
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
			QaVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("idx", vo.getQa_idx());
			jsonObject.addProperty("title", vo.getQa_title());
			jsonObject.addProperty("id", vo.getQa_id());
			jsonObject.addProperty("view", vo.getQa_view());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getQa_date()));
			jsonObject.addProperty("state", vo.getQa_state());
			
			jsonArray.add(jsonObject);
		}
		
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
	// 검색 리스트 출력
	public String search_selectList(String sel, String text) {

		List<QaVO> list = new ArrayList<QaVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println(sel + " "+text);

		String sql = null;

		if (sel.equals("작성자")) {
			sql = "SELECT * FROM qa where qa_id like '%' || ? || '%'";
		}
		if (sel.equals("글제목")) {
			sql = "SELECT * FROM qa where qa_title like '%' || ? || '%'";
		}
		if (sel.equals("글내용")) {
			sql = "SELECT * FROM qa where qa_content like '%' || ? || '%'";
		}

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);
			
			//조건 입력
			pstmt.setString(1, text);

			//3.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				QaVO vo = new QaVO();
				//현재레코드값=>Vo저장
				vo.setQa_idx(rs.getInt("qa_idx"));
				vo.setQa_title(rs.getString("qa_title"));
				vo.setQa_id(rs.getString("qa_id"));
				vo.setQa_view(rs.getInt("qa_view"));
				vo.setQa_date(rs.getDate("qa_date"));
				vo.setQa_state(rs.getString("qa_state"));
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
			QaVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("idx", vo.getQa_idx());
			jsonObject.addProperty("title", vo.getQa_title());
			jsonObject.addProperty("id", vo.getQa_id());
			jsonObject.addProperty("view", vo.getQa_view());
			jsonObject.addProperty("state", vo.getQa_state());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getQa_date()));
			
			jsonArray.add(jsonObject);
		}
		
		System.out.println("{res:" + jsonArray.toString() + "}");
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
	String returns;
	
	public String insert_user(String title, String content) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int res = 0;
		System.out.println(title + content);
		String sql = "insert into qa values(qa_seq_idx.nextVal, ?, 'admin', ?, 0, 0, sysdate, default, default, 0)";
		
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle_test");
			
			//1.Connection얻어온다
			conn = ds.getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, title);
			//pstmt.setString(2, vo.getQa_id());
			pstmt.setString(2, content);
			
			res = pstmt.executeUpdate();

		} catch (Exception e) {
			returns = String.format("{res:[{'result':'%s'}]}", "fail");
			
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(res == 1) {
			returns = String.format("{res:[{'result':'%s'}]}", "success");
		}
		System.out.println("err:" + returns);
		
		return returns;
	}
}
