package com.ntth.socialnetwork.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntth.socialnetwork.entity.FriendShip;
import com.ntth.socialnetwork.entity.Notification;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
		
	@Query(value = "SELECT * FROM notification WHERE recipient_id = :recipientId ORDER BY time_sent DESC", nativeQuery = true)
	List<Notification> getByIdRecipient(@Param("recipientId") long recipientId);
	
	@Query(value = "SELECT * FROM notification WHERE sender_id = :senderId ORDER BY time_sent DESC", nativeQuery = true)
	List<Notification> getByIdSender(@Param("senderId") long senderId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE notification SET is_check = 2 WHERE is_check = 1", nativeQuery = true)
	int setAllWasChecked();
}