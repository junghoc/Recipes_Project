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

public class BoardDAO {
	
	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static BoardDAO single = null;

	public static BoardDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new BoardDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	//레시피 리스트
	public String board_selectList() {

		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from (select rank() over(order by b_idx desc) no, d.* "
				+ "from (SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx ) d)";

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
				vo.setLike(rs.getInt("b_like"));;
				//ArrayList추가
				list.add(vo);
			}

		} catch (Exception e) {
			// TODO: handle exception
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
				// TODO Auto-generated catch block
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
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getDate()));
			jsonObject.addProperty("like", vo.getLike());
			jsonArray.add(jsonObject);
		}
		System.out.println("{res:" + jsonArray.toString() + "}");

		return "{res:" + jsonArray.toString() + "}";
	}
		
	//레시피 리스트
	public String board_selectone(String idx) {

		BoardVO vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx And b_idx = ?";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 설정
			pstmt.setString(1, idx);
					
			//4.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				vo = new BoardVO();
				//현재레코드값=>Vo저장
				vo.setIdx(rs.getInt("b_idx"));
				vo.setName(rs.getString("b_name"));
				vo.setContent(rs.getString("b_content"));
				vo.setM_id(rs.getString("id"));
				vo.setDate(rs.getDate("regi_date"));
				vo.setLike(rs.getInt("b_like"));;
			
			}

		} catch (Exception e) {
			// TODO: handle exception
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//list를 JSON으로 변환
		JsonArray jsonArray = new JsonArray();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("idx", vo.getIdx());
		jsonObject.addProperty("title", vo.getName());
		jsonObject.addProperty("content", vo.getContent());
		jsonObject.addProperty("id", vo.getM_id());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		jsonObject.addProperty("date",format.format(vo.getDate()));
		jsonObject.addProperty("like", vo.getLike());
		jsonArray.add(jsonObject);
		
		System.out.println("{res:" + jsonArray.toString() + "}");

		return "{res:" + jsonArray.toString() + "}";
	}
	
	//선택 게시글 좋아요
	public String board_like_update(String idx) {
		// TODO Auto-generated method stub
		int res = 0;
		String returns = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE BOARD set b_like = b_like + 1 WHERE b_idx = ?";

		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 채우기
			pstmt.setString(1, idx);
			
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
	
	//선택 게시글싫어요
	public String board_nolike_update(String idx) {
		// TODO Auto-generated method stub
		int res = 0;
		String returns = "";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE BOARD set b_like = b_like - 1 WHERE b_idx = ?";
		
		try {
			//1.Connection획득
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체 획득
			pstmt = conn.prepareStatement(sql);
			
			//3.pstmt parameter 채우기
			pstmt.setString(1, idx);
			
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
	
	//업데이트한 정보 가지고오기
	public String board_like_select(String idx) {

		BoardVO vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT b.b_like FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx And b_idx = ?";

		try {
			//1.Connection얻어온다
			conn = DBService.getInstance().getConnection();
			//2.명령처리객체정보를 얻어오기
			pstmt = conn.prepareStatement(sql);

			//3.pstmt parameter 설정
			pstmt.setString(1, idx);
					
			//4.결과행 처리객체 얻어오기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				vo = new BoardVO();
				//현재레코드값=>Vo저장
				vo.setLike(rs.getInt("b_like"));;
			
			}

		} catch (Exception e) {
			// TODO: handle exception
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//list를 JSON으로 변환
		JsonArray jsonArray = new JsonArray();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("like", vo.getLike());
		jsonArray.add(jsonObject);
		
		System.out.println("{res:" + jsonArray.toString() + "}");

		return "{res:" + jsonArray.toString() + "}";
	}	
	
	
}
