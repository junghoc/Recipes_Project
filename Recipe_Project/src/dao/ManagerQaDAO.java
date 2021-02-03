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
import vo.QaVO;

public class ManagerQaDAO {
	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static ManagerQaDAO single = null;

	public static ManagerQaDAO getInstance() {
		//생성되지 않았으면 생성
		if (single == null)
			single = new ManagerQaDAO();
		//생성된 객체정보를 반환
		return single;
	}
	
	// 	QA매니저 출력
	public String selectList() {
		System.out.println("리스트");

		List<QaVO> list = new ArrayList<QaVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from qa";

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
				vo.setQa_state(rs.getString("qa_state"));
				vo.setQa_answer_state(rs.getString("qa_answer_state"));
				vo.setQa_date(rs.getDate("qa_date"));
				
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
			QaVO vo = list.get(i);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("idx", vo.getQa_idx());
			jsonObject.addProperty("title", vo.getQa_title());
			jsonObject.addProperty("id", vo.getQa_id());
			jsonObject.addProperty("view", vo.getQa_view());
			jsonObject.addProperty("state", vo.getQa_state());
			jsonObject.addProperty("answer_state", vo.getQa_answer_state());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			jsonObject.addProperty("date",format.format(vo.getQa_date()));
			
			jsonArray.add(jsonObject);
		}
		
		System.out.println("{res:" + jsonArray.toString() + "}");
		return "{res:" + jsonArray.toString() + "}"	;
	}
	
}
