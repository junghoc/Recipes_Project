package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import service.DBService;
import vo.MemberVO;

public class LoginDAO {
	// single-ton pattern: 
		// 객체1개만생성해서 지속적으로 서비스하자
		static LoginDAO single = null;

		public static LoginDAO getInstance() {
			//생성되지 않았으면 생성
			if (single == null)
				single = new LoginDAO();
			//생성된 객체정보를 반환
			return single;
		}

		

		// 로그인을 위한 SELECT문
		public String selectOne(String id, String pwd) {

			MemberVO vo = null;
			String returns = "";
			String result = "no";
				
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from member where id = ? and pwd = ? and del_info = 0";

			try {
				//1.Connection얻어온다
				conn = DBService.getInstance().getConnection();
				//2.명령처리객체정보를 얻어오기
				pstmt = conn.prepareStatement(sql);

				//3.pstmt parameter 설정
				pstmt.setString(1, id);
				pstmt.setString(2, pwd);

				//4.결과행 처리객체 얻어오기
				rs = pstmt.executeQuery();

				if (rs.next()) {
					vo = new MemberVO();
					//현재레코드값=>Vo저장
					vo.setMember_idx(rs.getInt("member_idx"));
					vo.setId(id);
					vo.setPwd(pwd);
					vo.setName(rs.getString("name"));
					vo.setGender(rs.getString("gender"));
					vo.setBirth(rs.getString("birth"));
					vo.setPhone(rs.getString("phone"));
					vo.setEmail(rs.getString("email"));
					vo.setReg_date(rs.getString("reg_date"));
					vo.setMgr(rs.getInt("mgr"));
					vo.setDel_info(rs.getInt("del_info"));
				}
				
				if(vo==null) {
					result = "no";
					returns = String.format("{res:[{'result':'%s'}]}", result);	
				}else {
					result = "yes";
					returns = String.format("{res:[{'result':'%s'},{'member_idx':'%d'},{'id':'%s'},{'pwd':'%s'},{'name':'%s'},{'gender':'%s'},{'birth':'%s'},{'phone':'%s'},{'email':'%s'},{'reg_date':'%s'},{'mgr':'%d'},{'del_info':'%d'}]}", result,
							vo.getMember_idx(),vo.getId(),vo.getPwd(),vo.getName(),vo.getGender(),vo.getBirth(),vo.getPhone(),vo.getEmail(),vo.getReg_date(),vo.getMgr(),vo.getDel_info());
								
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				returns = String.format("{res:[{'result':'%s'}]}", result);
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


			return returns;
		}


		// 아이디 중복을 위한 SELECT문
		public String idcheck(String id) {

			MemberVO vo = null;
			String returns = "";
			String result = "yes";
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from member where id = ?";

			try {
				//1.Connection얻어온다
				conn = DBService.getInstance().getConnection();
				//2.명령처리객체정보를 얻어오기
				pstmt = conn.prepareStatement(sql);

				//3.pstmt parameter 설정
				pstmt.setString(1, id);

				//4.결과행 처리객체 얻어오기
				rs = pstmt.executeQuery();

				if (rs.next()) {
					vo = new MemberVO();
					//현재레코드값=>Vo저장
					vo.setMember_idx(rs.getInt("member_idx"));
					vo.setId(id);
					vo.setPwd(rs.getString("pwd"));
					vo.setName(rs.getString("name"));
					vo.setGender(rs.getString("gender"));
					vo.setBirth(rs.getString("birth"));
					vo.setPhone(rs.getString("phone"));
					vo.setEmail(rs.getString("email"));
					vo.setReg_date(rs.getString("reg_date"));
					vo.setMgr(rs.getInt("mgr"));
					vo.setDel_info(rs.getInt("del_info"));
				}
				
				if(vo==null) {
					result = "yes";
					returns = String.format("{res:[{'result':'%s'}]}", result);
				}else {
					result = "no";
					returns = String.format("{res:[{'result':'%s'}]}", result);
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result = "yes";
				returns = String.format("{res:[{'result':'%s'}]}", result);
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

			return returns;
		}
		
		// 회원 등록
		public String reg_member(MemberVO vo) {
			// TODO Auto-generated method stub
			int res = 0;
			String returns = "";
			String result = "no";

			Connection conn = null;
			PreparedStatement pstmt = null;

			String sql = "insert into member values(seq_member_idx.nextVal,?,?,?,?,?,?,?,sysdate,0,0)";

			try {
				//1.Connection획득
				conn = DBService.getInstance().getConnection();
				//2.명령처리객체 획득
				pstmt = conn.prepareStatement(sql);

				//3.pstmt parameter 채우기
				pstmt.setString(1, vo.getId());
				pstmt.setString(2, vo.getPwd());
				pstmt.setString(3, vo.getName());
				pstmt.setString(4, vo.getGender());
				pstmt.setString(5, vo.getBirth());
				pstmt.setString(6, vo.getPhone());
				pstmt.setString(7, vo.getEmail());
				
				//4.DB로 전송(res:처리된행수)
				res = pstmt.executeUpdate();
				
				if(res==1) {
					result = "yes";
					returns = String.format("{res:[{'result':'%s'}]}", result);
				}
							
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				returns = String.format("{res:[{'result':'%s'}]}", result);
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
			return returns;
		}
		
		// 아이디 찾기
		public String find_id(String phone, String name) {
			String returns = "";
			MemberVO vo = null;
			String result = "no";

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from member where phone = ? and name = ? and del_info = 0";

			try {
				//1.Connection얻어온다
				conn = DBService.getInstance().getConnection();
				//2.명령처리객체정보를 얻어오기
				pstmt = conn.prepareStatement(sql);

				//3.pstmt parameter 설정
				pstmt.setString(1, phone);
				pstmt.setString(2, name);

				//4.결과행 처리객체 얻어오기
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					vo = new MemberVO();
					//현재레코드값=>Vo저장
					vo.setMember_idx(rs.getInt("member_idx"));
					vo.setId(rs.getString("id"));
					vo.setPwd(rs.getString("pwd"));
					vo.setName(rs.getString("name"));
					vo.setGender(rs.getString("gender"));
					vo.setBirth(rs.getString("birth"));
					vo.setPhone(rs.getString("phone"));
					vo.setEmail(rs.getString("email"));
					vo.setReg_date(rs.getString("reg_date"));
					vo.setMgr(rs.getInt("mgr"));
					vo.setDel_info(rs.getInt("del_info"));				
				}
				
				// vo가 null이라면 없는 아이디이니 아이디가 없다고 확정
	 			if(vo!=null) {
					result = "yes";
					returns = String.format("{res:[{'result':'%s'},{'id':'%s'}]}", result,vo.getId());
	 				return returns;
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

			return returns;
		}
		
		// 비번찾기 찾기
		public String find_pwd(String id, String phone, String name) {

			String returns = "";
			MemberVO vo = null;
			String result = "no";

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from member where id = ? and phone = ? and name = ? and del_info = 0";

			try {
				//1.Connection얻어온다
				conn = DBService.getInstance().getConnection();
				//2.명령처리객체정보를 얻어오기
				pstmt = conn.prepareStatement(sql);

				//3.pstmt parameter 설정
				pstmt.setString(1, id);
				pstmt.setString(2, phone);
				pstmt.setString(3, name);

				//4.결과행 처리객체 얻어오기
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					vo = new MemberVO();
					//현재레코드값=>Vo저장
					vo.setMember_idx(rs.getInt("member_idx"));
					vo.setId(rs.getString("id"));
					vo.setPwd(rs.getString("pwd"));
					vo.setName(rs.getString("name"));
					vo.setGender(rs.getString("gender"));
					vo.setBirth(rs.getString("birth"));
					vo.setPhone(rs.getString("phone"));
					vo.setEmail(rs.getString("email"));
					vo.setReg_date(rs.getString("reg_date"));
					vo.setMgr(rs.getInt("mgr"));
					vo.setDel_info(rs.getInt("del_info"));				
				}
				
				// vo가 null이라면 없는 아이디이니 아이디가 없다고 확정
	 			if(vo==null) {
	 				returns = String.format("{res:[{'result':'%s'}]}", result);
	 				return returns;
				}else {
					result = "yes";
					returns = String.format("{res:[{'result':'%s'},{'pwd':'%s'}]}", result,vo.getPwd());
	 				return returns;
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				// 오류가 났다는 건 일단 no
				returns = String.format("{res:[{'result':'%s'}]}", result);
				
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

			return returns;
		}
		
	}
