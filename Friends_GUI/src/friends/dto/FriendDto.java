package friends.dto;

import java.sql.Date;

public class FriendDto {
	private int num;
	private String name;
	private String phone;
	private String regDate;
	
	public FriendDto() {
		
	}
	
	public FriendDto(int num, String name, String phone, String regDate) {
		super();
		this.num = num;
		this.name = name;
		this.phone = phone;
		this.regDate = regDate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
