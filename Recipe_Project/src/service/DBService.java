package service;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBService {
	// single-ton pattern: 필요한 하나의 객체만 생성해두고 다른 클래스들에서 같은 객체를 사용하는 것
	// 객체1개만생성해서 지속적으로 서비스하자
	// 생성자를 private으로 하면 객체 생성이 불가능하다.	
	
	// Service : DB의 접속과정을 담당하는 클래스
	
	// single-ton pattern: 
	// 객체1개만생성해서 지속적으로 서비스하자
	static DBService single = null;
	
	// getInstance로만 Service객체 생성이 가능하다.
	public static DBService getInstance() {
		//생성되지 않았으면 생성 (null은 new가 된 적이 없음을 뜻한다.)
		if (single == null)
			single = new DBService();
		//생성된 객체정보를 반환
		return single;
	}
	
	
	DataSource ds;
	
	// getInstance와 같다.
	private DBService() {
		// context가 없거나 <context>가 아닐 수도 있으니 try-catch 처리해준다.
		try {
			InitialContext ic = new InitialContext();
			// 준비한 JMDI에서 DATASOURCE를 찾는 식
			// jdbc/oracle_test명칭의 리소스를 참조(해당 계정으로 로그인을 시도)
			ds = (DataSource)ic.lookup("java:comp/env/jdbc/oracle_test");
			
			
		} catch (Exception e) {

		}
	} // 생성자
	
	// DB에 실제로 접속
	public Connection getConnection() {
		Connection conn = null;
		
		// 연결이 안되거나 sql문장이 잘못될 오류가능성이 있으니 try-catch 처리해준다.
		try {
			conn = ds.getConnection();
		} catch (Exception e) {
			
		}
		return conn;
	}
	// 실제 connection까지 처리한다.
}
