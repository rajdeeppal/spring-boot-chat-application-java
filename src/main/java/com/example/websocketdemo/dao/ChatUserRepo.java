package com.example.websocketdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.model.ChatMessage;
@Repository
public interface ChatUserRepo extends JpaRepository<ChatMessage, Integer> {

}
