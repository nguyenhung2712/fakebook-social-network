package com.ntth.socialnetwork.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Reply;
import com.ntth.socialnetwork.repository.ReplyRepository;

@RestController
@RequestMapping("/api/reply")
@CrossOrigin
public class ReplyController {

	@Autowired
	private ReplyRepository replyRepo;

	public ReplyController(ReplyRepository replyRepo) {
		super();
		this.replyRepo = replyRepo;
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@GetMapping("/{comment_id}")
	ResponseEntity<List<Reply>>getAllCommentOnPost(@PathVariable("comment_id") Long commend_id) {
		List<Reply> listComments = replyRepo.SelectRepliesOnComment(commend_id);
        return new ResponseEntity<List<Reply>>(listComments, HttpStatus.OK);

	}
	
	/*
	@GetMapping("/reply/{id}")
	ResponseEntity<?> getReplyById(@PathVariable("id") Long id) {
		Optional<Reply> reply = this.replyRepo.findById(id);
		return reply.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	*/
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@PostMapping("/add")
	ResponseEntity<Reply> createReply(@RequestBody Reply reply) throws URISyntaxException {
		reply.setDateReply(new java.sql.Timestamp(System.currentTimeMillis()));
		Reply result = replyRepo.save(reply);
		return ResponseEntity.created(new URI("/api/reply" + result.getId())).body(result); 
	}
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	ResponseEntity<Reply> updateReply(@PathVariable("id") Long id,@RequestBody Reply reply){
		Reply replyupdate = replyRepo.findById(id).orElseThrow();
		
		
		replyupdate.setDateReply(new java.sql.Timestamp(System.currentTimeMillis()));
		replyupdate.setReply(reply.getReply());
		replyupdate.setUser(reply.getUser());
		replyupdate.setComment(reply.getComment());

		Reply result = replyRepo.save(replyupdate);
		return ResponseEntity.ok().body(result);
	}
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	@DeleteMapping("/remove/{id}")
	ResponseEntity<?> deleteReply(@PathVariable Long id){
		replyRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
