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

public class ManagerBoardDAO {

	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static ManagerBoardDAO single = null;

	public static ManagerBoardDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new ManagerBoardDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	public String selectList() {

		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx AND b.status = 0";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVO vo = new BoardVO();
				//현재레코드값=>Vo저장
				vo.setIdx(rs.getInt("b_idx"));
				vo.setName(rs.getString("b_name"));
				vo.setM_id(rs.getString("id"));
				vo.setDate(rs.getDate("regi_date"));
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
			BoardVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("idx", vo.getIdx());
			jsonObject.addProperty("title", vo.getName());
			jsonObject.addProperty("id", vo.getM_id());
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getDate()));
			jsonArray.add(jsonObject);
		}
		
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
	public String selectList_search(String search) {

		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx AND b.status = 0 AND b.b_name LIKE '%' || ? || '%' ";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, search);

			//3.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVO vo = new BoardVO();
				//현재레코드값=>Vo저장
				vo.setIdx(rs.getInt("b_idx"));
				vo.setName(rs.getString("b_name"));
				vo.setM_id(rs.getString("id"));
				vo.setDate(rs.getDate("regi_date"));
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
			BoardVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("idx", vo.getIdx());
			jsonObject.addProperty("title", vo.getName());
			jsonObject.addProperty("id", vo.getM_id());
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getDate()));
			jsonArray.add(jsonObject);
		}
		
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
	public String selectOne(String idx) {

		BoardVO vo = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx AND b.b_idx = ?";

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
				vo = new BoardVO();
				//현재레코드값=>Vo저장
				vo.setIdx(rs.getInt("B_IDX"));
				vo.setName(rs.getString("B_NAME"));
				vo.setContent(rs.getString("B_CONTENT"));
				vo.setM_idx(rs.getInt("M_IDX"));
				vo.setRef(rs.getInt("B_REF"));
				vo.setStep(rs.getInt("B_STEP"));
				vo.setDepth(rs.getInt("B_DEPTH"));
				vo.setDate(rs.getDate("REGI_DATE"));
				vo.setLike(rs.getInt("B_LIKE"));
				vo.setStatus(rs.getInt("STATUS"));
				vo.setM_id(rs.getString("ID"));

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
		jsonObject.addProperty("idx", vo.getIdx());
		jsonObject.addProperty("title", vo.getName());
		jsonObject.addProperty("content", vo.getContent());
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		jsonObject.addProperty("date",format.format(vo.getDate()) );
		jsonObject.addProperty("like", vo.getLike());
		jsonObject.addProperty("writer", vo.getM_id());
		String status = "NORMAL";
		if (vo.getStatus() != 0) {
			status = "DELETEED";
		}
		jsonObject.addProperty("status", status);
		
		jsonArray.add(jsonObject);
		
		return "{res:" + jsonArray.toString() + "}";
	}
	
	public String board_status_update(String idx, String status) {
		// TODO Auto-generated method stub
		int res = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE board SET status = ? WHERE b_idx = ?";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, status);
			pstmt.setString(2, idx);

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
		
		if( res == 0 ) {
			return "{res:[{'result':'no'}]}"; 
		}
		
		return "{res:[{'result':'yes'}]}";
	}
	
	public String insert_board(String title, String context) {
		int res = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO board VALUES(seq_board_idx.nextval, ?, ?,"
				+ "1, seq_board_idx.currval, 0, 0, sysdate, default, default )";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, title);
			pstmt.setString(2, context);

			//4.DB로 전송(res:처리된행수)
			res = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
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
		
		if( res == 0 ) {
			return "{res:[{'result':'no'}]}"; 
		}
		
		return "{res:[{'result':'yes'}]}";
	}
}
