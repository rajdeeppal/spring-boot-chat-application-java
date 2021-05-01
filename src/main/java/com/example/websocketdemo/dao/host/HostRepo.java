package com.example.websocketdemo.dao.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.model.host.HostDetails;

@Repository
public interface HostRepo extends JpaRepository<HostDetails, Integer>{
	
	HostDetails findByName(String name);
}
