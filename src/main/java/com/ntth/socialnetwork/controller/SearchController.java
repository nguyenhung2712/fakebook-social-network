package com.ntth.socialnetwork.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.entity.Post;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.repository.PostRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000/")
public class SearchController {
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@GetMapping("/search/{keyword}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	ResponseEntity<HashMap<String, Object>> getUserProfileByKey(@PathVariable("keyword") String keyword){
		//list kết quả trả về
		HashMap<String, Object> listSearch = new HashMap<String, Object>();
		List<HashMap<String, Object>> listPostWithUser = new ArrayList<>();
		List<UserProfile> listAllUserProfile = this.userProfileRepo.findAll();
		List<UserProfile> listUserProfile= this.userProfileRepo.findByKeyword(keyword); // list user with search
		List<Post> listPost = this.postRepo.findByKeyword(keyword);// list post with search
		
		if (!listPost.isEmpty()) {
			for(Post post : listPost) {
				for(UserProfile up : listAllUserProfile) {
					if(post.getUser().getId().equals(up.getUser().getId())) {
						HashMap<String, Object> PostWithUser = new HashMap<String, Object>();
						PostWithUser.put("post", post);
						PostWithUser.put("userProfile", up);
						listPostWithUser.add(PostWithUser);
					}
				}
			}
		}

		//lưu các thông tin vào list
		listSearch.put("keyword", keyword);
		listSearch.put("userProfiles", listUserProfile);
		listSearch.put("posts", listPostWithUser);
        return ResponseEntity.ok().body(listSearch);
	}
	
}