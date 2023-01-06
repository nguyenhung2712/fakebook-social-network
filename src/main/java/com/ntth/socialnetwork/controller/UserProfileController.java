package com.ntth.socialnetwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.repository.UserProfileRepository;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class UserProfileController {

	@Autowired
	private UserProfileRepository userProfileRepo;

	public UserProfileController(UserProfileRepository userProfileRepo) {
		super();
		this.userProfileRepo = userProfileRepo;
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<List<UserProfile>> getAllProfile() {
		List<UserProfile> listProfile = this.userProfileRepo.findAll();
		return ResponseEntity.ok().body(listProfile);
	}
	
	@GetMapping("/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<UserProfile> getProfileByUserId(@PathVariable("user_id") Long user_id) {
		UserProfile profile = this.userProfileRepo.getProfileWithUserID(user_id);
		return ResponseEntity.ok().body(profile);
	}
	
	
	@PutMapping("/update/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<UserProfile> updateProfile(@PathVariable("user_id") Long user_id, @RequestBody UserProfile profile){
		UserProfile profileUpdate = userProfileRepo.getProfileWithUserID(user_id);
		
		profileUpdate.setUserProfileID(profile.getUserProfileID());
		profileUpdate.setFirstName(profile.getFirstName());
		profileUpdate.setLastName(profile.getLastName());
		profileUpdate.setGender(profile.getGender());
		profileUpdate.setDateOfBirth(profile.getDateOfBirth());
		profileUpdate.setAvatar(profile.getAvatar());
		profileUpdate.setBackground(profile.getBackground());
		profileUpdate.setAbout(profile.getAbout());

		UserProfile result = userProfileRepo.save(profileUpdate);
		return ResponseEntity.ok().body(result);
	}

	
	@GetMapping("/friendprofile/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<List<UserProfile>> getFriendProfileByUserID(@PathVariable("user_id") Long user_id) {
		List<UserProfile> listProfile = this.userProfileRepo.getFriendProfile(user_id);
		return ResponseEntity.ok().body(listProfile);
	}
}