package com.example.websocketdemo.model;




import java.time.LocalTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChatMessage {
	
	

	@Id
	@GeneratedValue
	private int MessageID ;
    private MessageType type;
    private String content;
    private String sender;
    LocalTime time = LocalTime.now();
    private String MeetingName ;
    
    public String getMeetingName() {
		return MeetingName;
	}
	public void setMeetingName(String meetingName) {
		MeetingName = meetingName;
	}
	public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
	public int getMessageID() {
		return MessageID;
	}
	public void setMessageID(int messageID) {
		MessageID = messageID;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
    

    
}
