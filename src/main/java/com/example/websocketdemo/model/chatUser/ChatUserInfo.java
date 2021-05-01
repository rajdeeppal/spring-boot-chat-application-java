package com.example.websocketdemo.model.chatUser;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class ChatUserInfo {
	@Id
	@GeneratedValue
	private int userID;
	private String name ;
    LocalTime joiningTime = LocalTime.now();
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalTime getJoiningTime() {
		return joiningTime;
	}
	public void setJoiningTime(LocalTime joiningTime) {
		this.joiningTime = joiningTime;
	}

	
	
	
}
