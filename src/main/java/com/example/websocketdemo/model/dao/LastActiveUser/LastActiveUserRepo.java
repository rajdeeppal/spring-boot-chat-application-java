package com.example.websocketdemo.model.dao.LastActiveUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.mode.LastActiveUser.LastActiveUserDetails;
@Repository
public interface LastActiveUserRepo extends JpaRepository<LastActiveUserDetails, Integer>{

}
