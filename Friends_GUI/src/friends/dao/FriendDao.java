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
		//�۾��� �������θ� ���� ����
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//������ sql��
			String sql = "INSERT INTO friends (num,name,phone,regdate)" + "VALUES(friends_seq.NEXTVAL,?,?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			// ? �� ���ε��ϱ�
			pstmt.setString(1, friendDto.getName());
			pstmt.setString(2, friendDto.getPhone());
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//�۾� �����̸�
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
		//���� ���θ� ����
		return isSuccess;
	}
	
	public boolean delete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//�۾��� �������θ� ���� ����
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//������ sql��
			String sql = "DELETE FROM friends WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			// ? �� ���ε��ϱ�
			pstmt.setInt(1, num);
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//�۾� �����̸�
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
		//���� ���θ� ����
		return isSuccess;		
	}
	
	public boolean update(FriendDto friendDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//�۾��� �������θ� ���� ����
		boolean isSuccess = false;
		
		try {
			conn = new DBConnect().getConn();
			//������ sql��
			String sql = "UPDATE friends SET name=?, phone=?, regdate = TO_DATE(?,'yyyy\"�� \"MM\"�� \"DD\"�� \"HH24:MI:SS')" + "WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			// ? �� ���ε��ϱ�
			pstmt.setString(1, friendDto.getName());
			pstmt.setString(2, friendDto.getPhone());
			pstmt.setString(3, friendDto.getRegDate());
			pstmt.setInt(4, friendDto.getNum());
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag = pstmt.executeUpdate();
			if(flag > 0) {//�۾� �����̸�
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
		//���� ���θ� ����
		return isSuccess;
	}
	
	public List<FriendDto> getList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//FriendDto ��ü�� ���� Vector ��ü����
		List<FriendDto> friends = new ArrayList<>();
				
		try {
			conn = new DBConnect().getConn();
			//������ sql��
			String sql = "SELECT num, name, phone, TO_CHAR(regdate, 'YYYY\"�� \"MM\"�� \"DD\"�� \"HH24:MI:SS') RD FROM friends ORDER BY num DESC";
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
