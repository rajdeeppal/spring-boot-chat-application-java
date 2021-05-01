package com.example.websocketdemo.dao.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.model.assignment.Assignment;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Integer>{

	Assignment findByReceiverAndAssignmentno(String receiver,String assignmentno);
	
	Assignment findByMailsendstatusAndSubmissionstatus(String mailsendstatus , String submissionstatus);
	
	Assignment findByReceiver(String receiver);
}
