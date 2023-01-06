package com.ntth.socialnetwork.controller;

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

import com.ntth.socialnetwork.entity.PostComment;
import com.ntth.socialnetwork.repository.PostCommentRepository;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

	@Autowired
	private PostCommentRepository commentRepo;
	

	
	public CommentController(PostCommentRepository commentRepo) {
		super();
		this.commentRepo = commentRepo;
	}

	
	@GetMapping("/{post_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<List<PostComment>>getAllCommentOnPost(@PathVariable("post_id") Long post_id) {
		List<PostComment> listComments = commentRepo.SelectCommentsOnPost(post_id);
        return new ResponseEntity<List<PostComment>>(listComments, HttpStatus.OK);

	}
	
	/*
	@GetMapping("/comment/{id}")
	ResponseEntity<?> getCommentById(@PathVariable("id") Long id) {
		Optional<Comment> comment = this.commentRepo.findById(id);
		return comment.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	*/
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) throws URISyntaxException {
		
		comment.setCommentDate(new java.sql.Timestamp(System.currentTimeMillis()));
		
		PostComment result = commentRepo.save(comment);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<PostComment> updateComment(@PathVariable("id") Long id,@RequestBody PostComment comment){
		PostComment updatecomment = commentRepo.findById(id).orElseThrow();
		
		
		updatecomment.setContent(comment.getContent());
		updatecomment.setCommentDate(new java.sql.Timestamp(System.currentTimeMillis()));
		updatecomment.setPost(comment.getPost());
		updatecomment.setUser(comment.getUser());

		
		PostComment result = commentRepo.save(updatecomment);
		
		return ResponseEntity.ok().body(result);
	}
	@DeleteMapping("/remove/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> deleteComment(@PathVariable Long id){	
		commentRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	
}