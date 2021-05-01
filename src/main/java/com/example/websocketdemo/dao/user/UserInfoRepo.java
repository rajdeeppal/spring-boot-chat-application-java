package com.example.websocketdemo.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.model.chatUser.ChatUserInfo;

@Repository
public interface UserInfoRepo extends JpaRepository<ChatUserInfo, Integer>{

	ChatUserInfo findByName(String userName);
}
