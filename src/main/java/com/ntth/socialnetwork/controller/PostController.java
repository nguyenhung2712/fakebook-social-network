package com.ntth.socialnetwork.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.ntth.socialnetwork.entity.EditHistory;
import com.ntth.socialnetwork.entity.LikePost;
import com.ntth.socialnetwork.entity.Post;
import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.payload.request.PostRequest;
import com.ntth.socialnetwork.repository.EditHistoryRepository;
import com.ntth.socialnetwork.repository.LikePostRepository;
import com.ntth.socialnetwork.repository.PostRepository;
import com.ntth.socialnetwork.repository.UserRepository;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;

	@Autowired
	EditHistoryRepository editDetailRepo;
	
	@Autowired
	LikePostRepository likeRepository;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Post>> getAllPosts() {
		try {
			List<Post> posts = new ArrayList<Post>();
			postRepository.findAll().forEach(posts::add);
	
			if (posts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/detail/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getPostById(@PathVariable("id") Long id) {
		Optional<Post> post = postRepository.findById(id);
		
		if (post.isPresent()) {
			return new ResponseEntity<>(post.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Post>>getPostByUserId(@PathVariable("user_id") Long user_id) {
		List<Post> listPost = this.postRepository.getPostsWithUserID(user_id);
        
        return new ResponseEntity<List<Post>>(listPost, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> createPost(@Valid @RequestBody PostRequest postRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User currentUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		String content = postRequest.getContent();
		String image = postRequest.getImage();
		try {
			Set<Post> posts = new HashSet<Post>();
			Set<EditHistory> postsChange = new HashSet<EditHistory>();
			
			EditHistory editDetail = editDetailRepo.save(
					new EditHistory(currentDate, content, image, posts));
			postsChange.add(editDetail);
			
			Post newPost = postRepository
					.save(new Post(content, image, 
							currentDate, currentUser, postsChange, null));
			posts.add(newPost);
			editDetail.setPosts(posts);
			
			editDetailRepo.save(editDetail);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add-post-group/{group_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> createGroupPost(
			@Valid @RequestBody PostRequest postRequest,
			@PathVariable("group_id") Long group_id
		) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User currentUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		String content = postRequest.getContent();
		String image = postRequest.getImage();
		
		try {	
			Set<Post> posts = new HashSet<Post>();
			Set<EditHistory> postsChange = new HashSet<EditHistory>();
			
			EditHistory editDetail = editDetailRepo.save(
					new EditHistory(currentDate, content, image, posts));
			postsChange.add(editDetail);
			
			Post newPost = postRepository.save(
					new Post(content, image, 
							currentDate, currentUser, postsChange, group_id));
			return new ResponseEntity<>(newPost, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<HttpStatus> updatePost(
			@PathVariable("id") long id, 
			@RequestBody PostRequest postRequest
		) {
		Optional<Post> postData = postRepository.findById(id);
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		String content = postRequest.getContent();
		String image = postRequest.getImage();
		if (postData.isPresent()) {
			Set<Post> posts = new HashSet<Post>();
			EditHistory editDetail = editDetailRepo.save(
					new EditHistory(currentDate, content, image, posts));
			
			Post post = postData.get();
			Set<EditHistory> oldPostschange = post.getPostsChange();
			post.setContent(postRequest.getContent());
			post.setImage(postRequest.getImage());
			post.setPublishedDate(currentDate);
			
			oldPostschange.add(editDetail);
			post.setPostsChange(oldPostschange);
			Post updatedPost = postRepository.save(post);
			
			posts.add(updatedPost);
			
			editDetailRepo.save(editDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/remove/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deletePostById(@PathVariable("id") Long id) {
		try {
			postRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-history/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getEditHistoryByPostId(@PathVariable("id") Long id) {
		try {
			List<EditHistory> editDetails = editDetailRepo.getEditHistoryDetails(id);
			
			return new ResponseEntity<>(editDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-post-group/{group_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Post>> getAllPostsGroup(@PathVariable("group_id") Long group_id) {
		try {
			List<Post> posts = this.postRepository.getPostsGroup(group_id);
	
			if (posts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get-by-friend/{user_id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Post>>getFriendPostByUserID(@PathVariable("user_id") Long user_id) {
		List<Post> listPostFriend = this.postRepository.getFriendPostByUserID(user_id);
		List<Post> listPostUser = this.postRepository.getPostsWithUserID(user_id);
		listPostUser.addAll(listPostFriend);
            return new ResponseEntity<List<Post>>(listPostUser, HttpStatus.OK);
		
	}
	
	
	@GetMapping("/post-liked/{user-id}/{post-id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<LikePost> getPostUserLiked(@PathVariable("user-id") Long userId,@PathVariable("post-id") Long postId) {
		try {
			LikePost post = likeRepository.getPostsUserLiked(userId,postId);
			return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
		} catch (Exception e) {	
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/year-register")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> getYearPublishedByPost() {
		try {
			List<Long> years = postRepository.getYearByPost();
			return new ResponseEntity<List<Long>>(years, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/count-by-year")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> countPostByYearPublished() {
		try {
			List<Long> countPosts = postRepository.countPostByYear();
			return new ResponseEntity<List<Long>>(countPosts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/month-register/{year}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<String>> getMonthPublishedByPost(@PathVariable("year") Long year) {
		try {
			List<String> months = postRepository.getMonthByPost(year);
			return new ResponseEntity<List<String>>(months, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/count-by-month/{year}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<Long>> countPostByMonthPublished(@PathVariable("year") Long year) {
		try {
			List<Long> countPosts = postRepository.countPostByMonth(year);
			return new ResponseEntity<List<Long>>(countPosts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
