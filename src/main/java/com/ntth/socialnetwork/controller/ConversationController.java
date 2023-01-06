package com.ntth.socialnetwork.controller;

import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.ntth.socialnetwork.entity.ConverJoinDetails;
import com.ntth.socialnetwork.entity.Conversation;
import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.payload.request.AddUsersToConvRequest;
import com.ntth.socialnetwork.payload.request.ConversationRequest;
import com.ntth.socialnetwork.repository.ConvJoinDetailsRepository;
import com.ntth.socialnetwork.repository.ConversationRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;
import com.ntth.socialnetwork.repository.UserRepository;


@RestController
@CrossOrigin
@RequestMapping("/api/conversation")
public class ConversationController {

	@Autowired
	private ConversationRepository conversationRepository;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConvJoinDetailsRepository convJoinRepo;
	
	@GetMapping("/{userID}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Conversation>> getAllConversationsList(@PathVariable("userID") Long userID) {
		try {
			List<Conversation> allConversations = conversationRepository.getConversationByUserID(userID);
			return new ResponseEntity<List<Conversation>>(allConversations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/mem-qtt/{conv_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getMemberQttInConv(@PathVariable("conv_id") Long conv_id) {
		try {
			Long membersQtt = conversationRepository.getMemberQttInConv(conv_id);
			return new ResponseEntity<>(membersQtt, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) throws URISyntaxException {
		Conversation result = conversationRepository.save(conversation);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@GetMapping("/{conv_id}/other-mems-profile/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getProfileOfOtherConvMembers(
			@PathVariable("conv_id") Long conv_id,
			@PathVariable("user_id") Long user_id) {
		try {
			List<UserProfile> profiles = userProfileRepo.getProfileOfOtherConvMembers(conv_id, user_id);
			return new ResponseEntity<>(profiles, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{conv_id}/other-mems/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getOtherConvMembers(
			@PathVariable("conv_id") Long conv_id,
			@PathVariable("user_id") Long user_id) {
		try {
			List<User> users = userRepository.getOtherConvMembers(conv_id, user_id);
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add-room")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> createRoomConv(
			@RequestBody AddUsersToConvRequest request) {
		try {
			String convName = new String();
			
			List<Long> membersID = request.getMembersID();
			List<User> members = new ArrayList<>();
			Set<ConverJoinDetails> cjList = new HashSet<>();
			
			for (int i = 0; i < membersID.size(); i++) {
				Long id = membersID.get(i);
				User user = userRepository.findById(id).get();
				UserProfile profile = userProfileRepo.findByUserID(id);
				convName += profile.getFirstName().concat(" " + profile.getLastName()) + " ";
				members.add(user);
			}
			
			Conversation newConver = conversationRepository.save(
					new Conversation(convName, 1)
			);
			
			members.forEach(member -> {
				ConverJoinDetails newConverJoin = 
						new ConverJoinDetails(newConver, member, new Date(System.currentTimeMillis()));
				cjList.add(newConverJoin);
				convJoinRepo.save(newConverJoin);
			});
			newConver.setCjDetailsList(cjList);
			conversationRepository.save(newConver);
			
			return new ResponseEntity<>(newConver, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/{conv_id}/friends-not-joined/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getProfileOfFriendNotJoinedConv(
			@PathVariable("conv_id") Long conv_id,
			@PathVariable("user_id") Long user_id) {
		try {
			List<UserProfile> profiles = userProfileRepo.getProfileOfFriendNotJoinedConv(conv_id, user_id);
			return new ResponseEntity<>(profiles, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/update-room/{conv_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateConversation(
			@RequestBody ConversationRequest request,
			@PathVariable("conv_id") Long conv_id
	) {
		try {
			Optional<Conversation> converData = conversationRepository.findById(conv_id);
			if (converData.isPresent()) {
				Conversation conver = converData.get();
				conver.setName(request.getName());
				conver.setStatus(request.getStatus());
				return new ResponseEntity<>(conversationRepository.save(conver), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/remove-room/{conv_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> removeConversation(@PathVariable("conv_id") Long conv_id) {
		try {
			Optional<Conversation> converData = conversationRepository.findById(conv_id);
			conversationRepository.delete(converData.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add-to/{conv_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> addToConver(
			@RequestBody AddUsersToConvRequest request,
			@PathVariable("conv_id") Long conv_id
	) {
		try {
			List<Long> membersID = request.getMembersID();
			Optional<Conversation> converData = conversationRepository.findById(conv_id);
			List<UserProfile> newMemberProfiles = new ArrayList<>();
			
			if (converData.isPresent()) {
				Conversation conver = converData.get();
				membersID.forEach(memberID -> {
					User user = userRepository.findById(memberID).get();
					UserProfile profile = userProfileRepo.getProfileWithUserID(user.getId());
					
					convJoinRepo.save(
							new ConverJoinDetails(conver, user, new Date(System.currentTimeMillis()))
					);
					newMemberProfiles.add(profile);
				});
				
				return new ResponseEntity<>(newMemberProfiles, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{user_id}/remove-from/{conv_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> removeFromConver(
			@PathVariable("user_id") Long user_id,
			@PathVariable("conv_id") Long conv_id
	) {
		try {
			Conversation conver = conversationRepository.findById(conv_id).get();
			User user = userRepository.findById(user_id).get();
			
			Optional<ConverJoinDetails> joinConv = convJoinRepo.findByConverAndUser(conver, user);
			if (joinConv.isPresent()) {
				convJoinRepo.delete(joinConv.get());
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}	
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	@GetMapping("/{conv_id}/other-mem-ids/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getOtherMemIDs(
			@PathVariable("conv_id") Long conv_id,
			@PathVariable("user_id") Long user_id) {
		try {
			List<Long> users = conversationRepository.getOtherUserID(user_id,conv_id);
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
