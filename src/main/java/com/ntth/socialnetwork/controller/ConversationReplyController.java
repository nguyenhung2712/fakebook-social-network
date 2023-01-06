package com.ntth.socialnetwork.controller;

import java.net.URISyntaxException;
import java.util.List;

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

import com.ntth.socialnetwork.entity.ConversationReply;
import com.ntth.socialnetwork.repository.ConversationReplyRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/conversation/reply")
public class ConversationReplyController {

	@Autowired
	private ConversationReplyRepository conversationReplyRepository;
	
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<ConversationReply>> getAllConversationRepliesList() {
		try {
			List<ConversationReply> allConversationReplies = conversationReplyRepository.findAll();
			return new ResponseEntity<List<ConversationReply>>(allConversationReplies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/user/{conversationID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<ConversationReply>> getConversationReplies(@PathVariable("conversationID") Long conversationID) {
		try {
			List<ConversationReply> allConversationReplies = conversationReplyRepository.getConversationReplies(conversationID);
			return new ResponseEntity<List<ConversationReply>>(allConversationReplies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{conversationID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ConversationReply> getLastConversationReply(@PathVariable("conversationID") Long conversationID) {
		try {
			ConversationReply lastConversationReply = conversationReplyRepository.getLastConversationReply(conversationID);
			return new ResponseEntity<ConversationReply>(lastConversationReply, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<ConversationReply> createComment(@RequestBody ConversationReply reply) throws URISyntaxException {
		ConversationReply result = conversationReplyRepository.save(reply);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/remove/{conversationReplyID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void deleteMessage(@PathVariable("conversationReplyID") Long conversationReplyID) {
			conversationReplyRepository.deleteMessage(conversationReplyID);
	}
	
	
	@GetMapping("/getlast")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Long getLastID() {
		return conversationReplyRepository.getLastID();
	}
	
	@GetMapping("/countnewmessage/{conversationID}/{userID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Long getCountNewMessage(@PathVariable("conversationID") Long conversationID,@PathVariable("userID") Long userID) {
		return conversationReplyRepository.getCountNewMessage(conversationID,userID);
	}
	
	////
	@GetMapping("/total-new-mess/{userID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Long getTotalNewMessage(@PathVariable("userID") Long userID) {
		return conversationReplyRepository.getTotalNewMessage(userID);
	}
	////
	
	@GetMapping("/{conversationID}/other-user-id/{userID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Long> getOthersUserID(@PathVariable("conversationID") Long conversationID,
			@PathVariable("userID") Long userID) {
		return conversationReplyRepository.getOthersUserID(conversationID,userID);
	}
	
	@PutMapping("/updatestatus/{conversationID}/{userID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void updateStatus(@PathVariable("conversationID") Long conversationID,@PathVariable("userID") Long userID) {
		conversationReplyRepository.updateStatusReceiver(conversationID,userID);
	}
	
}