package com.ntth.socialnetwork.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.FriendShip;
import com.ntth.socialnetwork.entity.Notification;
import com.ntth.socialnetwork.entity.PostComment;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.repository.NotificationRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
public class NotificationController {
	
	@Autowired
	private NotificationRepository notiRepo;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Notification>> getNotificationList() {
		List<Notification> listNoti = notiRepo.findAll();
		return ResponseEntity.ok().body(listNoti);
	}
	
	@GetMapping("/recipient/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Notification>> getNotificationRecipientList(@PathVariable("id") Long recipientId) {
		List<Notification> listNotiReceipt = notiRepo.getByIdRecipient(recipientId);
		return ResponseEntity.ok().body(listNotiReceipt);
	}
	
	@GetMapping("/sender/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Notification>> getNotificationSenderList(@PathVariable("id") Long senderId) {
		List<Notification> listNotiSender = notiRepo.getByIdSender(senderId);
		return ResponseEntity.ok().body(listNotiSender);
	}
	
	//add notidication
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> createNotification(@RequestBody Notification notification){
		notification.setTimeSent(new java.sql.Date(System.currentTimeMillis()));
		Notification result = notiRepo.save(notification);
		return ResponseEntity.ok().body(result);	
	}
	
	
	//read comment
	@PutMapping("/readed/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> setWasReaded(@PathVariable("id") Long id){
		Optional<Notification> updateNoti = notiRepo.findById(id);
		if (updateNoti.isPresent()) {
			Notification notiUp = updateNoti.get();
			notiUp.setIsRead(2);
			Notification result = notiRepo.save(notiUp);
	        return ResponseEntity.ok()
				      .body(result);
		} else {
			return ResponseEntity.badRequest()
				      .body(null);
		}
	}
	
	@PutMapping("/checked")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> setAllWasChecked(){
		int a = notiRepo.setAllWasChecked();
		return ResponseEntity.ok().body(a);
	}
	
}