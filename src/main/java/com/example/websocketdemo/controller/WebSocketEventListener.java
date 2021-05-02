package com.example.websocketdemo.controller;

import com.example.websocketdemo.dao.ChatUserRepo;
import com.example.websocketdemo.dao.emails.UserEmailRepo;
import com.example.websocketdemo.dao.host.HostRepo;
import com.example.websocketdemo.dao.user.UserInfoRepo;
import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.model.chatUser.ChatUserInfo;
import com.example.websocketdemo.model.email.userEmail;
import com.example.websocketdemo.model.host.HostDetails;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;



@Component
public class WebSocketEventListener {
	
	@Autowired
	UserInfoRepo userInfoRepo;
	
	@Autowired
	HostRepo hostRepo;
	
	
	@Autowired
	UserEmailRepo userEmailRepo;
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        if(username != null) {
            logger.info("User Disconnected : " + username);
            //new added
            ChatUserInfo chatUserInfo = userInfoRepo.findByName(username);
            if(chatUserInfo != null)
            	userInfoRepo.delete(chatUserInfo);          
            HostDetails hostDetails = hostRepo.findByName(username);
            if(hostDetails!= null) {
            	hostRepo.delete(hostDetails);
            if(userInfoRepo.count()>=1) {
            	List<ChatUserInfo>  u = userInfoRepo.findAll();
            	ChatUserInfo cui = u.get(0);
            	userEmail ue = userEmailRepo.findByName(cui.getName());
            	hostDetails.setName(ue.getName());
            	hostDetails.setHostEmail(ue.getEmailID());
            	hostRepo.save(hostDetails);
            }
            }
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
