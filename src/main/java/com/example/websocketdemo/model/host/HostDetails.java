package com.example.websocketdemo.model.host;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HostDetails {
	@Id
	@GeneratedValue
	private int HostId ;
	private String name ;
	private String HostEmail ;
	public int getHostId() {
		return HostId;
	}
	public void setHostId(int hostId) {
		HostId = hostId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHostEmail() {
		return HostEmail;
	}
	public void setHostEmail(String hostEmail) {
		HostEmail = hostEmail;
	}
	
	
}
