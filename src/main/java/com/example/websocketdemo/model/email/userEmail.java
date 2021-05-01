package com.example.websocketdemo.model.email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class userEmail {
	@Id
	@GeneratedValue
	private int ID ;
	private String name ;
	private String EmailID;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailID() {
		return EmailID;
	}
	public void setEmailID(String emailID) {
		EmailID = emailID;
	}
	
}
