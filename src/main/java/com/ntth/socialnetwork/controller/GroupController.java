package com.ntth.socialnetwork.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Group;
import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.payload.request.GroupRequest;
import com.ntth.socialnetwork.payload.response.MessageResponse;
import com.ntth.socialnetwork.repository.GroupJoinDetailsRepository;
import com.ntth.socialnetwork.repository.GroupRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;
import com.ntth.socialnetwork.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/group")
public class GroupController {
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GroupJoinDetailsRepository joindetailsRepo;
	@Autowired
	UserProfileRepository userProfileRepo;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Group>> getAllGroups() {
		try {
			List<Group> allGroups = groupRepository.findAll();
			return new ResponseEntity<List<Group>>(allGroups, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getGroupById(@PathVariable Long id) {
		try {
			Optional<Group> group = groupRepository.findById(id);
			
			if (group.isPresent()) {
				return new ResponseEntity<>(group.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> createGroup(@Valid @RequestBody GroupRequest groupRequest) {
		String groupName = groupRequest.getGroupName();
		if (groupRepository.existsByGroupName(groupName)) {
			return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Error: Your group name is already taken!"));
		}
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			User currentUser = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
			Group newGroup = groupRepository.save(
				new Group(groupRequest.getGroupName(), "...", 
						new java.sql.Date(System.currentTimeMillis()), currentUser)	
			);
			
			return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") long id, @RequestBody GroupRequest groupRequest) {
		String groupName = groupRequest.getGroupName();
		if (groupRepository.existsByGroupName(groupName)) {
			return ResponseEntity
			          .badRequest()
			          .body(new MessageResponse("Error: Your group name is already taken!"));
		}
		Optional<Group> groupData = groupRepository.findById(id);
		
		if (groupData.isPresent()) {
			Group group = groupData.get();
			group.setGroupName(groupRequest.getGroupName());
			group.setGroupAbout(groupRequest.getGroupAbout());
			return new ResponseEntity<>(groupRepository.save(group), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/remove/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deletePostById(@PathVariable("id") Long id) {
		try {
			groupRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}/total-member")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getTotalMember(@PathVariable("id") Long id) {
		try {
			Long total = joindetailsRepo.getTotalMemberOfGroup(id);
			return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/user-joined/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Group>> getGroupsUserJoined(@PathVariable("id") Long id) {
		try {
			List<Group> groups = groupRepository.getGroupsUserJoined(id);
			return new ResponseEntity<>(groups, HttpStatus.ACCEPTED);
		} catch (Exception e) {	
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{user_id}/other-member-profile/{group_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<UserProfile>> getProfileOfGroupMembers(
			@PathVariable("user_id") Long user_id,
			@PathVariable("group_id") Long group_id
			) {
		try {
			List<UserProfile> profiles = userProfileRepo.getProfileOfGroupMembers(group_id, user_id);
			return new ResponseEntity<>(profiles, HttpStatus.ACCEPTED);
		} catch (Exception e) {	
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}