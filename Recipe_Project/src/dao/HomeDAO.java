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

public class HomeDAO {

	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static HomeDAO single = null;

	public static HomeDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new HomeDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	//메인화면 리스트
	public String home_selectList() {

		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from (select rank() over(order by b_like desc) no, d.* "
				+ "from (SELECT b.* , m.id FROM BOARD b, MEMBER m WHERE b.m_idx = m.member_idx ) d) "
				+ "where no between 1 AND 5";

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
	
	
}
