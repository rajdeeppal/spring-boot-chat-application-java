package com.example.websocketdemo.controller;

import com.example.websocketdemo.EmailConfig.NotificationService;
import com.example.websocketdemo.dao.ChatUserRepo;
import com.example.websocketdemo.dao.assignment.AssignmentRepo;
import com.example.websocketdemo.dao.emails.UserEmailRepo;
import com.example.websocketdemo.dao.host.HostRepo;
import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.model.assignment.Assignment;
import com.example.websocketdemo.dao.user.UserInfoRepo;
import com.example.websocketdemo.mode.LastActiveUser.LastActiveUserDetails;
import com.example.websocketdemo.model.chatUser.ChatUserInfo;
import com.example.websocketdemo.model.dao.LastActiveUser.LastActiveUserRepo;
import com.example.websocketdemo.model.email.userEmail;
import com.example.websocketdemo.model.host.HostDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class ChatController {

	@Autowired
	ChatUserRepo repo ;
	
	
	@Autowired
	UserInfoRepo userInfoRepo ;
	
	@Autowired
	UserEmailRepo userEmailRepo ; 
	
	@Autowired
	LastActiveUserRepo lastActiveUserRepo ;
	
	@Autowired
	HostRepo hostRepo ;
	
	@Autowired
	AssignmentRepo assignmentRepo ;
	
	@Autowired
	private NotificationService notificationService ;
//	

	
	
	
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage , @Payload userEmail user,@Payload HostDetails hostDetails,@Payload ChatUserInfo chatUserInfo,@Payload LastActiveUserDetails lastActiveUserDetails ,@Payload Assignment assignment) {
    	//repo.save(chatMessage);
    	Boolean flag = false;
    	String message = chatMessage.getContent();
    	String normfont = message ;
    	message = message.toLowerCase();
    	String setTitle = new String("set meeting name");
    	String changeTitle = new String("change meeting name");
    	String changeHost = new String("change host");
    	if(lastActiveUserRepo.count() < 1 ) {
    		lastActiveUserDetails.setRefMeetingName("Public");
    		lastActiveUserDetails.setRefSender("Root");
    		lastActiveUserRepo.save(lastActiveUserDetails);
    	}
    
		userEmail U = userEmailRepo.findByName(chatMessage.getSender());
		HostDetails hdetails = hostRepo.findByName(chatMessage.getSender());
    	//set the meeting name 
    	if(message.contains(setTitle)) {
    			if(hdetails == null ) {
    				chatMessage.setContent("you are not host !!");
    			}
    			else {
            		int i = message.indexOf('#');
            		if(!message.contains("#") || i == message.length() - 1 ) {
            			chatMessage.setContent("Wrong command!!!");
            			flag = true ;
            		}
            		else {
            			
            		StringBuilder str = new StringBuilder();
            		for( int j = i + 1 ; j < message.length() ; ++j ) {
            			str.append(message.charAt(j));
            		}
            		String title = str.toString();
            		title = title.toUpperCase();
            		List<LastActiveUserDetails> list = lastActiveUserRepo.findAll();
            		LastActiveUserDetails last = list.get(0);
            		String lastTitle = last.getRefMeetingName();
            		if(lastTitle.matches("Public")) {
            			lastActiveUserRepo.deleteAll();
            			lastActiveUserDetails.setRefMeetingName(title);
            			lastActiveUserDetails.setRefSender(chatMessage.getSender());
            			lastActiveUserRepo.save(lastActiveUserDetails);
            			String content = chatMessage.getSender() + " has set a new meeting name "+ title ;
            			chatMessage.setContent(content);
            			flag = true ;
            		}
            		else {
            			chatMessage.setContent("Meeing name is already set!!");
            			flag = true ;
            			}
            		      		
            		}
    				
    			}
    			
    		}

    	
    	// change the meeting name 
    	else if(message.contains(changeTitle)) {
			if(hdetails == null ) {
				chatMessage.setContent("you are not host !!");
				flag = true;
			}
			
			else {
        		int i = message.indexOf('#');
        		
        		if(!message.contains("#") || i == message.length() - 1 ) {
        			chatMessage.setContent("Wrong command!!!");
        			flag = true ;
        		}
        		else { 
        		StringBuilder str = new StringBuilder();
        		for( int j = i + 1 ; j < message.length() ; ++j ) {
        			str.append(message.charAt(j));
        		}
        		String title = str.toString();
        		if(title.length() > 1 ) {

        		
        		title = title.toUpperCase();
        		List<LastActiveUserDetails> list = lastActiveUserRepo.findAll();
        		LastActiveUserDetails last = list.get(0);
        		String lastTitle = last.getRefMeetingName();
        		if(!lastTitle.matches("Public")) {
        			lastActiveUserRepo.deleteAll();
        			lastActiveUserDetails.setRefMeetingName(title);
        			lastActiveUserDetails.setRefSender(chatMessage.getSender());
        			lastActiveUserRepo.save(lastActiveUserDetails);
        			String content = chatMessage.getSender() + " has change the meeting name "+ lastTitle + " to "+ title ;
        			chatMessage.setContent(content);
        			flag = true ;
        		}
        		else {
        			chatMessage.setContent("Meeing name is not yet set!!");
        			flag = true ;
        		}
        			
        		}
        		}	
			}
	
    		}
    	
    	//change the host
    	else if(message.contains(changeHost)) {
    		
        		int i = message.indexOf('@');
        		
        		if(!message.contains("@") || i == message.length() - 1 ) {
        			chatMessage.setContent("Wrong command!!!");
        			flag = true ;
        		}
        		
        		else {
        			HostDetails hstDetails = hostRepo.findByName(chatMessage.getSender());
        			if(hstDetails != null ) {
        				
        				StringBuilder str = new StringBuilder();
                		for( int j = i + 1 ; j < message.length() ; ++j ) {
                			if(message.charAt(j)!= ' ')
                				str.append(message.charAt(j));
                		}
                		String newhost = str.toString();
                		newhost = newhost.substring(0,1).toUpperCase() + newhost.substring(1);
                		user = userEmailRepo.findByName(newhost);
                		chatUserInfo = userInfoRepo.findByName(newhost);
                		if(chatUserInfo != null ) {
                			
                		System.out.println(newhost);
                		hostDetails.setName(newhost);
                		user = userEmailRepo.findByName(newhost);
                		String newhostemail = user.getEmailID();
                		hostDetails.setHostEmail(newhostemail);
                		hostRepo.deleteAll();
                		hostRepo.save(hostDetails);
                		chatMessage.setContent(newhost + " you are now host!!");
                		flag = true ;
                		}
                		else if(user == null ) {
                			chatMessage.setContent(newhost + " is not a register user !!");
                			flag = true ;
                		}
                			
                		else {
                			chatMessage.setContent(newhost + " has not joined yet!!")  ;
                			flag = true ;
                		}
                			
        			}
        			else {
        				chatMessage.setContent("You are not Host!!");
        				flag = true ;
        			}
        				
            		
        		}
    	}
    	
    	//Assignments
    	else if(message.contains("Assignment") || message.contains("assignment") && (message.contains("@") || message.contains("#") || message.contains("$"))) {
    		
    		HostDetails hstDetails = hostRepo.findByName(chatMessage.getSender());
    		
    		
    		List< LastActiveUserDetails > detail = lastActiveUserRepo.findAll();
			LastActiveUserDetails lastdetail = detail.get(0);
			String sr = lastdetail.getRefMeetingName();
			
			System.out.println("meeting name "+sr);
    		if(hstDetails != null && !sr.matches("Public")) {
    			System.out.println("satisfied");
    			Queue<Integer> Qat = new LinkedList<>();
        		Queue<Integer> Qhash = new LinkedList<>();
        		Queue<Integer> Qdoll = new LinkedList<>();

        		int index = message.indexOf("assignment");
        		
        		for( int i = 0 ; i < message.length() ; ++i ) {
        			if(message.charAt(i) == '@')
        				Qat.add(i);
        			else if(message.charAt(i)=='#')
        				Qhash.add(i);
        			else if(message.charAt(i)=='$')
        				Qdoll.add(i);
        			
        		}
        		StringBuilder s = new StringBuilder();
        		for( int j = index + 11 ; j < Qat.peek() ; ++j ) {
        			if(normfont.charAt(j) != ' ' || normfont.charAt(j) != ':' || normfont.charAt(j) != '-') {
        				s.append(normfont.charAt(j));
        			}
        		}
        		
        		String S = s.toString();
        		S = S.replaceAll("\\s", "");
        		System.out.println("len of : " + S);
        		if(Qat.isEmpty() || Qhash.isEmpty() || Qdoll.isEmpty() && (Qat.size() != Qhash.size() || Qhash.size() != Qdoll.size())){
        			chatMessage.setContent("Use @=> Assignee #=> Assignment $=> Deadline!!");
        			flag = true ;
        		}
        		while(!Qat.isEmpty() && !Qhash.isEmpty() && !Qdoll.isEmpty() ) {
        			//assignment = new Assignment();
        			String recname = normfont.substring(Qat.peek()+1, Qhash.peek() - 1 ) ;
        			recname = recname.replaceAll("\\s", "");
        			List<String> names = new ArrayList<String>();
        			if(recname.length() < 2 ) {
        				List<userEmail> uemail = userEmailRepo.findAll();
        				for(userEmail u : uemail) {
        					if(!u.getName().matches(chatMessage.getSender()))
        						names.add(u.getName());
        				}
        			}
        			//new module
        			else if(recname.length() > 2 ) {
            			String str[] = recname.split(",");
            			names = Arrays.asList(str);
        			}
        			for( String n : names ) {
        				System.out.println(n);
        			}

//        			 user = userEmailRepo.findByName(recname);
//        			 if(user == null ) {
//        				chatMessage.setContent("Invalid details!!");
//         				flag = true ;
//         				break;
//        			 }
        			 
        			if(S.isEmpty() ) {
        				chatMessage.setContent("Mention assignment no!!");
        				flag = true ;
        				break;
        			}
   
        			else {
        				//assignment 1 @Rajdeep #m $2020-11-11
        				//receiver name
            			//--assignment.setReceiver(recname); String receivername = recname ;
            			//content
            			//--assignment.setContent(normfont.substring(Qhash.peek() + 1, Qdoll.peek() - 1 )); 
            			String contentname = normfont.substring(Qhash.peek() + 1, Qdoll.peek() - 1 );

            			Qat.remove();
            			String submissiondate ="";

            			if(!Qat.isEmpty()) {
            				// submission date
            				//--assignment.setSubmissiondate(normfont.substring(Qdoll.peek() + 1 , Qat.peek() - 1 )); 
            				submissiondate = normfont.substring(Qdoll.peek() + 1 , Qat.peek() - 1 );
            			}
            			else {
            				// submission date
            				//--assignment.setSubmissiondate(normfont.substring(Qdoll.peek() + 1 ));
            				submissiondate = normfont.substring(Qdoll.peek() + 1 );
            			}
               			//System.out.println("hi hello");
            			// sender name
            			//--assignment.setSender(chatMessage.getSender()); 
 
            			String sendername = chatMessage.getSender();
 
            			user = userEmailRepo.findByName(sendername);
            			// sender email
            			//--assignment.setSenderId(user.getEmailID()); 
            			String senderid = user.getEmailID();
            			
            			
            			for(String receivername : names) {
            				assignment = new Assignment();
            				receivername = receivername.replaceAll("\\s", "");
            				user = userEmailRepo.findByName(receivername);
            				if(user != null) {
	            				assignment.setReceiver(receivername);
	            				assignment.setContent(contentname);
	            				assignment.setSubmissiondate(submissiondate);
	            				assignment.setSender(sendername);
	            				assignment.setSenderId(senderid);
	            				assignment.setReceiverId(user.getEmailID());
	            				assignment.setMailsendstatus("pending");
	                			assignment.setSubmissionstatus("not submitted");
	                			assignment.setAssignmentno(S);
	                			List< LastActiveUserDetails > details = lastActiveUserRepo.findAll();
	                			LastActiveUserDetails lastdetails = details.get(0);
	                			// assignment topic
	                			assignment.setTopic(lastdetails.getRefMeetingName());
	                			Assignment assign = assignmentRepo.findByReceiverAndAssignmentno(receivername,assignment.getAssignmentno());
	                			if( assign != null ) {
	                				assignmentRepo.delete(assign);
	                				assignmentRepo.save(assignment);
	                			}
	                			else 
	                				assignmentRepo.save(assignment);
            				}
            			}
            			
//            			user = userEmailRepo.findByName(assignment.getReceiver());
//            			//receiver email
//            			assignment.setReceiverId(user.getEmailID());
//            			
//            			assignment.setMailsendstatus("pending");
//            			assignment.setSubmissionstatus("not submitted");
//            			//assignment no
//            			assignment.setAssignmentno(S);
//            			List< LastActiveUserDetails > details = lastActiveUserRepo.findAll();
//            			LastActiveUserDetails lastdetails = details.get(0);
//            			// assignment topic
//            			assignment.setTopic(lastdetails.getRefMeetingName()); // set meeting name in assignment
            			Qhash.remove();
            			Qdoll.remove();
//            			Assignment assign = assignmentRepo.findByReceiverAndAssignmentno(assignment.getReceiver(),assignment.getAssignmentno());
//            			if( assign != null ) {
//            				assignmentRepo.delete(assign);
//            				assignmentRepo.save(assignment);
//            			}
//            			else 
//            				assignmentRepo.save(assignment);
        			}

        			//System.out.println("working");   				
        		}	
    		}
    		else {
    			chatMessage.setContent("You are not Host !! or meeting name not yet set !!");
    			flag = true ;
    		}
    		
    	}
    	
    	// send assignment
    	else if(message.contains("send assignment")) {
    		HostDetails hstDetails = hostRepo.findByName(chatMessage.getSender());
    		if(hstDetails != null ) {
        		List<Assignment> list = assignmentRepo.findAll();
        		List <String> names = new ArrayList<String>();
        		StringBuilder s = new StringBuilder();
        		for(Assignment as : list ) {
        			
        			if(as.getMailsendstatus().contains("pending")) {
        				notificationService.sendAssignment(as);
        				as.setMailsendstatus("send");
        				assignmentRepo.save(as);
        				if(!names.contains(as.getReceiver().toLowerCase())) {
        					names.add(as.getReceiver().toLowerCase());
        				}
        				//s.append(as.getReceiver() + " ,");
        			}
        				//System.out.println(as.getReceiverId());
        			}
        		for(int i = 0 ; i < names.size() - 1 ; ++i ) {
        			s.append(names.get(i).toUpperCase() + " ,");
        		}
        		s.append(names.get(names.size()-1).toUpperCase());
        		if(list.isEmpty())
        			chatMessage.setContent("No assignment is assign !!");
        		else {
        			String S = s.toString();
            		chatMessage.setContent("Assignment send to "+ S);
        		}
        		
    		}
    		else 
    			chatMessage.setContent("You are not host !!");
    		flag = true ;
    		}
    	//pending assignment
    	
    	else if(message.contains("pending assignment")) {
    		List<Assignment> list = assignmentRepo.findAll();
    		if(list.isEmpty()) {
    			chatMessage.setContent("No pending assignments!!");
    			flag = true ;
    		}
    			
    		else {
    			StringBuilder s = new StringBuilder();
    			int cnt = 0 ;
    			for(Assignment as : list ) {
        			if(as.getMailsendstatus().contains("send") && as.getSubmissionstatus().contains("not submitted") ) {
        				cnt += 1 ;
        				s.append(as.getReceiver() + " Assignment - " +as.getAssignmentno() + ", ");
        			}
        				
        		}
    			if(cnt < 1 )
    				chatMessage.setContent("Assignments not send Yet!!");
    			else {
    				String S = s.toString();
        			chatMessage.setContent("Pending Assignment:- \n " + S);
    			}
    			
    			flag = true ;
    		}
    		
    	}
    	
    	// submit assignment
    	else if(message.contains("submitted")) {
    		StringBuilder s = new StringBuilder();
    		for(int i = 0 ; i < message.length() ; ++i ) {
    			if(Character.isDigit(message.charAt(i)))
    				s.append(message.charAt(i));
    		}
    		String S = s.toString();
    		assignment = assignmentRepo.findByReceiver(chatMessage.getSender());
    		if(S.length()<1) {
    			chatMessage.setContent("Mention Assignment number!!");
    			flag = true ;
    		}
    		else if(assignment == null ) {
    			chatMessage.setContent("You don't have any assignment");
    			flag = true;
    		}
    		else 
    		{
    			assignment = assignmentRepo.findByReceiverAndAssignmentno(chatMessage.getSender(), S);
    			if(assignment.getSubmissionstatus().matches("submitted")) {
    				chatMessage.setContent("Already submitted!!");
    				flag = true ;
    			}
    			
    			else {
    				assignment.setSubmissionstatus("submitted");
    				assignmentRepo.save(assignment);
    				chatMessage.setContent("successfully submitted!!");
    				flag = true ;
    			}
    		}
    	}
    	// register
    	
    	else if(U == null ) {
    		String str[] = chatMessage.getContent().split(" ");
    		List<String> list = new ArrayList<String>();
    		list = Arrays.asList(str);
    		for( String s : list ) {
    			if(s.endsWith("@gmail.com")) {
    				userEmail u = new userEmail();
    				u.setEmailID(s);
    				u.setName(chatMessage.getSender());
    				userEmailRepo.save(u);
    				chatMessage.setContent("Registered!!");
    				chatUserInfo.setName(chatMessage.getSender());
    				chatUserInfo.setName(chatMessage.getSender());
    				userInfoRepo.save(chatUserInfo);
    				// make first newly register person automatically host 
    				if(userInfoRepo.count()==1) {
    					hostDetails.setName(chatMessage.getSender());
    					hostDetails.setHostEmail(s);
    					hostRepo.save(hostDetails);
    				}
    			}
    		}
    		flag = true ;
    	}
    	
    	else if(message.contains("help")) {
    		
    		chatMessage.setContent("1. set meeting name #< meeting name > " + "\n" + "2. change meeting name #<meeting name>"
    		
    				+ "\n" + "3. change host @<host name> " + "\n" + "4. assignment <assignment no> @<assignee name> #<assignment> @<dateline (YYYY-MM-DD)> " + "\n"
    				
    				+ "5. submitted <assignment no> " + "\n" + "6. send assignment " +"7. pending assignment " +"\n"
    				);
    		
    		flag = true;
    	}
    	else if( message.matches("end")) {
    		HostDetails hstDetails = hostRepo.findByName(chatMessage.getSender());
    		if(hstDetails != null ) {
    		hostRepo.deleteAll();
    		userInfoRepo.deleteAll();
    		chatMessage.setContent("Meeting ended every one can leave !!");
//    		lastActiveUserRepo.deleteAll();
    		}
    		else 
    			chatMessage.setContent("You are not host!!");
    		flag = true ;
    	}

    	List<LastActiveUserDetails> list = lastActiveUserRepo.findAll();
		LastActiveUserDetails last = list.get(0);
		chatMessage.setMeetingName(last.getRefMeetingName());
		userEmail u = userEmailRepo.findByName(chatMessage.getSender());
		if( u == null )
			chatMessage.setSender(chatMessage.getSender() + "(Anonymous)");
		//saving chat details
		if(!flag)
			repo.save(chatMessage);
		hostDetails = hostRepo.findByName(chatMessage.getSender());
		if(hostDetails != null)
			chatMessage.setSender(chatMessage.getSender() + "(host)");
        return chatMessage;
    }
//    @MessageMapping("/chat.register")
//    public void register(@Payload userEmail user ) {
//    	userEmailRepo.save(user);
// 
//    }
//    
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    
    public ChatMessage addUser(@Payload ChatMessage chatMessage, @Payload ChatUserInfo chatUserInfo, @Payload userEmail email,@Payload HostDetails hostDetails ,
                               SimpMessageHeaderAccessor headerAccessor) {
    
    	email = userEmailRepo.findByName(chatMessage.getSender());

    		
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            //System.out.println(chatMessage.getSender());
            
            System.out.println(userInfoRepo.count());
            
            	String host = chatMessage.getSender() ;
            	if( email != null ) {
            		chatUserInfo.setName(chatMessage.getSender());
                    
                    userInfoRepo.save(chatUserInfo);
                	userEmail user = userEmailRepo.findByName(host);
                	String hostEmail = user.getEmailID();
                	//System.out.println(user.getEmailID());
                	if(userInfoRepo.count() == 1 ) {
                		//send remainder
                		List<Assignment> li = assignmentRepo.findAll();
            			for(Assignment as : li ) {
                			if(as.getMailsendstatus().contains("send") && as.getSubmissionstatus().contains("not submitted") ) {
                				SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                				LocalDate date = LocalDate.now();
                				String d = date.toString();
                				try {
                					Date firstDate = sfd.parse(d);
                				    Date secondDate = sfd.parse(as.getSubmissiondate());
                				 
                				    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                				    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                				    System.out.println(diff);
                				    if(diff < 7 && diff > 0 ) {
                				    	notificationService.sendRemainder(as, diff);
                				    	System.out.println("remainder send to " + as.getReceiver());
                				    }
                				}
                				catch (ParseException e) {
                					System.out.println(e.getMessage());
                		
                				}	
                				
                			}		
                		}

                		hostDetails.setName(host);
                		hostDetails.setHostEmail(hostEmail);
                		hostRepo.save(hostDetails);
                		System.out.println(host + " email -> " + hostEmail + " is the host!! ");
                		List<userEmail> list = userEmailRepo.findAll();
                		for( userEmail itr : list ) {
                			if( !itr.getName().matches(host) ) {
                				try{ 
                					//notificationService.sendNotification(itr, host, hostEmail);
                				}
                				catch(MailException e ) {
                					System.out.println("Error sending mail " + e.getMessage());
                				}
                				//System.out.println(itr.getName() + " email -> " + itr.getEmailID());
                			}
                		}
                	}
            	}
            	
            
    	return chatMessage;

    }
    


}
