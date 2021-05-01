package com.example.websocketdemo.EmailConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.websocketdemo.model.assignment.Assignment;
import com.example.websocketdemo.model.email.userEmail;

@Service
public class NotificationService {

	private JavaMailSender javaMailSender ;
	
	@Autowired
	public NotificationService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	} 
	public void sendNotification( userEmail user , String host , String hostEmailID  ) throws MailException{
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmailID());
		mail.setFrom("chittaranjanpal049@gmail.com");
		mail.setSubject("Meeting sheduled") ;
		mail.setText(host + " has start the meeting join soon!!" );
		javaMailSender.send(mail);
		System.out.println("mail send successfully!!");
	}
	
	public void sendAssignment(Assignment assignment) throws MailException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(assignment.getReceiverId());
		mail.setFrom("chittaranjanpal049@gmail.com");
		mail.setSubject(assignment.getTopic() + " ASSIGNMNENT "+assignment.getAssignmentno());
		mail.setText(assignment.getContent() + "\n" + assignment.getReceiver() + " submit by " + assignment.getSubmissiondate());
		javaMailSender.send(mail);
		System.out.println("assignment send!!");
	}
	
	public void sendRemainder(Assignment assignment , long day) throws MailException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(assignment.getReceiverId());
		mail.setFrom("chittaranjanpal049@gmail.com");
		mail.setSubject("REMAINDER");
		if(day > 1 )
			mail.setText(day + " days left to submit your assignment!!");
		else
			mail.setText("Tomorrow is the last date to submit your assignment!!");
		javaMailSender.send(mail);
		System.out.println("remainder send!!");
	}
}
