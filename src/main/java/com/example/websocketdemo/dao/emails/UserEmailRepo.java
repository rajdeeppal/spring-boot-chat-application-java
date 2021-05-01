package com.example.websocketdemo.dao.emails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.model.email.userEmail;

@Repository
public interface UserEmailRepo extends JpaRepository<userEmail, Integer>{

	userEmail findByName(String name);
}
