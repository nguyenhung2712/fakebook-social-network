package com.ntth.socialnetwork.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.FriendShip;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.repository.FriendShipRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FriendController {

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private FriendShipRepository friendShipRepo;
	
	//--------------------------Friend-------------------------------
		//get list friend of user have {id}
	@GetMapping("/friend/list-friend/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> getListFriendById(@PathVariable("id") Long id){
		List<UserProfile> listFriend = this.userProfileRepo.getListFriend(id);
        return ResponseEntity.ok()
			      .body(listFriend);
	}
	
	//get list friendship
	@GetMapping("/friend/list-friendship/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> getListFriendShipById(@PathVariable("id") Long id){
		List<UserProfile> listFriendShip = this.userProfileRepo.getListFriendShip(id);
        return ResponseEntity.ok()
			      .body(listFriendShip);
	}
	
	//get list people add friend to user
	@GetMapping("/friend/list-requester/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> getListPeopleRequestFriendToUser(@PathVariable("id") Long id){
		List<UserProfile> listRequestFriend = this.userProfileRepo.getListRequestFriendToUser(id);
        return ResponseEntity.ok()
			      .body(listRequestFriend);
	}
	
	//get list user add friend
	@GetMapping("/friend/list-requesting/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> getListUserRequestToFriend(@PathVariable("id") Long id){
		List<UserProfile> listRequestFriend = this.userProfileRepo.getListUserRequestFriend(id);
        return ResponseEntity.ok()
			      .body(listRequestFriend);
	}
	
	//add friend id1 to id2
	@PostMapping("/friend/add-friend/{id1}/{id2}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> sendRequestFriend(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2){
		List<UserProfile> listFriendUserID1 = this.userProfileRepo.getListFriend(id1);
		//ki???m tra xem c??? 2 ???? l?? b???n ho???c ???? g???i k???t b???n cho nhau ch??a
		Optional<FriendShip> frShip = friendShipRepo.getByIdTwoUser(id1, id2);
		if (frShip.isEmpty()) {
			FriendShip friendShip = new FriendShip();
			friendShip.setUserID1(id1);
			friendShip.setUserID2(id2);
			friendShip.setStatusID(1); // 1 l?? tr???ng th??i g???i k???t b???n cho nhau, 2 l?? ???? l?? b???n b?? 
			friendShip.setDateAddFriend(new java.sql.Date(System.currentTimeMillis()));
	        FriendShip result = friendShipRepo.save(friendShip);
	        return ResponseEntity.ok()
				      .body(result);
		} else {
			return ResponseEntity.badRequest()
				      .body(null);
		}
	}
	
	//accept friend
	@PostMapping("/friend/accept-friend/{id1}/{id2}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> acceptFriend(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2){
		List<UserProfile> listFriendUserID1 = this.userProfileRepo.getListFriend(id1);
		//ki???m tra xem c??? 2 ???? l?? b???n ho???c ???? g???i k???t b???n cho nhau ch??a
		Optional<FriendShip> friendShip = friendShipRepo.getByIdTwoUser(id1, id2);
		if (friendShip.isPresent()) {
			friendShip.get().setStatusID(2);
			FriendShip result = friendShipRepo.save(friendShip.get());
	        return ResponseEntity.ok()
				      .body(result);
			
		} else {
			return ResponseEntity.badRequest()
				      .body(null);
		}
		
	}
	
	@DeleteMapping("/friend/delete-friendship/{id1}/{id2}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<?> deleteFriend(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2){
		Optional<FriendShip> friendShip = friendShipRepo.getByIdTwoUser(id1, id2);
		friendShipRepo.delete(friendShip.get());
		return 	ResponseEntity.ok()
			      .body(null);
		}
	
}
