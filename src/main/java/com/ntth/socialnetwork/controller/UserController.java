package com.ntth.socialnetwork.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Group;
import com.ntth.socialnetwork.entity.GroupJoinDetails;
import com.ntth.socialnetwork.entity.LikePost;
import com.ntth.socialnetwork.entity.Post;
import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.payload.request.LJGroupRequest;
import com.ntth.socialnetwork.payload.request.LikePostRequest;
import com.ntth.socialnetwork.payload.request.UserRequest;
import com.ntth.socialnetwork.repository.GroupJoinDetailsRepository;
import com.ntth.socialnetwork.repository.GroupRepository;
import com.ntth.socialnetwork.repository.LikePostRepository;
import com.ntth.socialnetwork.repository.PostRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;
import com.ntth.socialnetwork.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	GroupRepository groupRepo;
	@Autowired
	UserProfileRepository userprofileRepo;
	@Autowired
	GroupJoinDetailsRepository joindetailsRepo;
	@Autowired
	LikePostRepository likeRepo;
	@Autowired
	PostRepository postRepo;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getUsesList() {
		try {
			List<User> allUsers = userRepo.findAll();
			return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/get-by-user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getUserProfileByUserAccount(@Valid @RequestBody UserRequest userReq) {
		try {
			User user = userRepo.findByUsername(userReq.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userReq.getUsername()));
			Optional<UserProfile> profile = userprofileRepo.findByUser(user);
			if (profile.isPresent()) {
				return new ResponseEntity<>(profile.get(), HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/join-group")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<GroupJoinDetails> joinGroup(@Valid @RequestBody LJGroupRequest data) {
		try {
			User user = userRepo.findById(data.getUserId()).get();
			Group group = groupRepo.findById(data.getGroupId()).get();
			
			GroupJoinDetails joinDetails = joindetailsRepo.save(new GroupJoinDetails(group, user, 
					new java.sql.Date(System.currentTimeMillis())));
			return new ResponseEntity<GroupJoinDetails>(joinDetails, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/leave-group")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> leaveGroup(@Valid @RequestBody LJGroupRequest data) {
		try {
			User user = userRepo.findById(data.getUserId()).get();
			Group group = groupRepo.findById(data.getGroupId()).get();
			
			joindetailsRepo.deleteByGroupAndUser(group, user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{user_id}/check-joined/{group_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> checkUserJoinedGroup(
			@PathVariable("user_id") Long user_id,
			@PathVariable("group_id") Long group_id
			) {
		try {
			Long isJoined = joindetailsRepo.checkUserJoinedGroup(group_id, user_id);
			return new ResponseEntity<>(isJoined, HttpStatus.ACCEPTED);
		} catch (Exception e) {	
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{user_id}/check-admin/{group_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> checkUserIsAdminGroup(
			@PathVariable("user_id") Long user_id,
			@PathVariable("group_id") Long group_id
			) {
		try {
			Long isAdmin = groupRepo.checkUserIsAdminGroup(group_id, user_id);
			return new ResponseEntity<>(isAdmin, HttpStatus.ACCEPTED);
		} catch (Exception e) {	
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getByUserID(@PathVariable("id") Long id) {
		Optional<User> user = this.userRepo.findById(id);
		return user.map(res -> ResponseEntity.ok().body(res)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	
	
	@PostMapping("/like-post")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<LikePost> likePost(@Valid @RequestBody LikePostRequest data) {
		try {
			Post post = postRepo.findById(data.getPostId()).get();
			User user = userRepo.findById(data.getUserId()).get();
			
			
			LikePost likePostDetails = likeRepo.save(new LikePost(post, user, 
					new java.sql.Date(System.currentTimeMillis())));
			return new ResponseEntity<LikePost>(likePostDetails, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/dislike-post/{post_id}/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void dislikePost(@PathVariable("post_id") Long post_id, @PathVariable("user_id") Long user_id) {
		try {
			likeRepo.dislikePost(post_id, user_id);
		} 
		catch (Exception e) {
			System.out.print(e);
		}
	}
	
	@GetMapping("/year-register")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> getYearRegisterByUser() {
		try {
			List<Long> years = userRepo.getYearByUser();
			return new ResponseEntity<List<Long>>(years, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/count-by-year")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> countUserByYearRegister() {
		try {
			List<Long> countUsers = userRepo.countUserByYear();
			return new ResponseEntity<List<Long>>(countUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/month-register/{year}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<String>> getMonthRegisterByUser(@PathVariable("year") Long year) {
		try {
			List<String> months = userRepo.getMonthByUser(year);
			return new ResponseEntity<List<String>>(months, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/count-by-month/{year}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> countUserByMonthRegister(@PathVariable("year") Long year) {
		try {
			List<Long> countUsers = userRepo.countUserByMonth(year);
			return new ResponseEntity<List<Long>>(countUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-username")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<String>> getUserName() {
		try {
			List<String> usernames = userRepo.getListUserName();
			return new ResponseEntity<List<String>>(usernames, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-role")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<String>> getRole() {
		try {
			List<String> roles = userRepo.getListRole();
			return new ResponseEntity<List<String>>(roles, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/year-comment")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> getNewCommentYear() {
		try {
			List<Long> countUsers = userRepo.getTimeComment();
			return new ResponseEntity<List<Long>>(countUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
