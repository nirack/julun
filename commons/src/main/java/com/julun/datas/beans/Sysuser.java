package com.julun.datas.beans;

public class Sysuser {


	/**
	 * 
	 */
	private Integer userid;
	
	
	/**
	 * 
	 */
	private String username;
	
	
	/**
	 * 
	 */
	private String password;
	
	
	/**
	 * 
	 */
	private String email;
	
	
	/**
	 * 
	 */
	private java.util.Date birthday;
	
	
	public Integer getUserid(){
		return this.userid;
	}
	
	public void setUserid(Integer userid ){
		this.userid = userid;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username ){
		this.username = username;
	}
	
	public String getPassword(){
		return this.password;
	}

	public void setPassword(String password ){
		this.password = password;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email ){
		this.email = email;
	}

	public java.util.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

}
