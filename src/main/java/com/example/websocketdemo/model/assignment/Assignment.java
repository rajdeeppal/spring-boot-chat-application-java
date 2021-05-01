package com.example.websocketdemo.model.assignment;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Assignment {
	
	@Id
	@GeneratedValue
	private int id ;
	private String sender ;
	private String senderId;
	private String receiver ;
	private String receiverId;
	private String content ;
	private String submissiondate;
	LocalDate assignmentdate = LocalDate.now();
	private String assignmentno ;
	private String mailsendstatus;
	private String submissionstatus ;
	private String topic ;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public LocalDate getAssignmentdate() {
		return assignmentdate;
	}
	public void setAssignmentdate(LocalDate assignmentdate) {
		this.assignmentdate = assignmentdate;
	}
	public String getAssignmentno() {
		return assignmentno;
	}
	public void setAssignmentno(String assignmentno) {
		this.assignmentno = assignmentno;
	}
	public String getMailsendstatus() {
		return mailsendstatus;
	}
	public void setMailsendstatus(String mailsendstatus) {
		this.mailsendstatus = mailsendstatus;
	}
	public String getSubmissionstatus() {
		return submissionstatus;
	}
	public void setSubmissionstatus(String submissionstatus) {
		this.submissionstatus = submissionstatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubmissiondate() {
		return submissiondate;
	}
	public void setSubmissiondate(String submissiondate) {
		this.submissiondate = submissiondate;
	}
	
	
}
