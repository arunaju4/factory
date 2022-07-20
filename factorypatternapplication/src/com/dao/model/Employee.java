package com.dao.model;

public class Employee {

	private int id;
	private String user;
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(int id, String user) {
		super();
		this.id = id;
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", user=" + user + "]";
	}
	
}
