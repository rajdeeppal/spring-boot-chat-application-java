package com.example.websocketdemo.mode.LastActiveUser;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LastActiveUserDetails {
	@Id
	@GeneratedValue
	private int RefID ;
	private String RefSender ;
	LocalTime time = LocalTime.now();
	private String RefMeetingName ;
	public int getRefID() {
		return RefID;
	}
	public void setRefID(int refID) {
		RefID = refID;
	}
	public String getRefSender() {
		return RefSender;
	}
	public void setRefSender(String refSender) {
		RefSender = refSender;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	public String getRefMeetingName() {
		return RefMeetingName;
	}
	public void setRefMeetingName(String refMeetingName) {
		RefMeetingName = refMeetingName;
	}
	
}
