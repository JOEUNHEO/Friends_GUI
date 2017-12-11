package friends.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import friends.dto.FriendDto;
import friends.util.DBConnect;

public class FriendDao {
	private static FriendDao friendDao;
	
	private FriendDao() {
		
	}
	
	public static FriendDao getInstance() {
		if(friendDao == null) {
			friendDao = new FriendDao();
		}
		
		return friendDao;
	}
	
	public boolean insert(FriendDto friendDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//작업의 성공여부를 담을 변수
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//실행할 sql문
			String sql = "INSERT INTO friends (num,name,phone,regdate)" + "VALUES(friends_seq.NEXTVAL,?,?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			// ? 에 바인딩하기
			pstmt.setString(1, friendDto.getName());
			pstmt.setString(2, friendDto.getPhone());
			// sql 문 수행하고 추가된 row 의 갯수 얻어오기
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//작업 성공이면
				isSuccess = true;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(conn != null)conn.close();
				if(pstmt != null)pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//성공 여부를 리턴
		return isSuccess;
	}
	
	public boolean delete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//작업의 성공여부를 담을 변수
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//실행할 sql문
			String sql = "DELETE FROM friends WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			// ? 에 바인딩하기
			pstmt.setInt(1, num);
			// sql 문 수행하고 추가된 row 의 갯수 얻어오기
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//작업 성공이면
				isSuccess = true;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(conn != null)conn.close();
				if(pstmt != null)pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//성공 여부를 리턴
		return isSuccess;		
	}
	
	public boolean update(FriendDto friendDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//작업의 성공여부를 담을 변수
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//실행할 sql문
			String sql = "UPDATE friends SET name=?, phone=?, regdate = TO_DATE(?,'yyyy\"년 \"MM\"월 \"DD\"일 \"HH24:MI:SS')" + "WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			// ? 에 바인딩하기
			pstmt.setString(1, friendDto.getName());
			pstmt.setString(2, friendDto.getPhone());
			pstmt.setString(3, friendDto.getRegDate());
			pstmt.setInt(4, friendDto.getNum());
			// sql 문 수행하고 추가된 row 의 갯수 얻어오기
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//작업 성공이면
				isSuccess = true;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(conn != null)conn.close();
				if(pstmt != null)pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//성공 여부를 리턴
		return isSuccess;
	}
	
	public List<FriendDto> getList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//FriendDto 객체를 담을 Vector 객체생성
		List<FriendDto> friends = new ArrayList<>();
				
		try {
			conn = new DBConnect().getConn();
			//실행할 sql문
			String sql = "SELECT num, name, phone, TO_CHAR(regdate, 'YYYY\"년 \"MM\"월 \"DD\"일 \"HH24:MI:SS') RD FROM friends ORDER BY num DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String regDate = rs.getString("RD");
								
				FriendDto friendDto = new FriendDto(num, name, phone, regDate);
				friends.add(friendDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(conn != null)conn.close();
				if(pstmt != null)pstmt.close();
				if(rs != null)rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return friends;
	}
}
