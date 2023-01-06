package com.ntth.socialnetwork.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "username", nullable = false)
	private String username;

  	@NotBlank
  	@Size(max = 50)
  	@Email
  	@Column(name = "email", nullable = false)
  	private String email;

  	@NotBlank
  	@Size(max = 120)
  	@Column(name = "password", nullable = false)
  	private String password;
  
  	@Column(name = "registered_date", nullable = false)
  	private Date registeredDate;
  	
  	@Column(name = "is_enabled")
  	private boolean isEnabled;
  	
  	@JsonIgnore
  	@OneToOne(mappedBy="user")
	private ConfirmationToken token;
  	
  	@JsonIgnore
  	@ManyToMany(fetch = FetchType.LAZY)
  	@JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  	private Set<Role> roles = new HashSet<>();
  
  	@JsonIgnore
  	@OneToMany(mappedBy="user")
  	private Set<Post> posts;
  	
  	@JsonIgnore
  	@OneToMany(mappedBy="user")
  	private Set<PostComment> comments;
  	
  	@JsonIgnore
  	@OneToMany(mappedBy="user_admin", cascade = CascadeType.ALL)
    private Set<Group> groups;
  	
  	@JsonIgnore
  	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
  	private Set<GroupJoinDetails> gpDetailsList;
  	
  	@JsonIgnore
  	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
  	private Set<ConverJoinDetails> cjDetailsList;
  	
  	@JsonIgnore
  	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
  	private Set<ConversationReply> conversationReply;
  	
  	@JsonIgnore
  	@OneToOne(mappedBy="user")
	private UserProfile profile;
  	
  	public User() {
  		
  	}

  	public User(String username, String email, String password, Date registeredDate, boolean isEnabled) {
	  this.username = username;
	  this.email = email;
	  this.password = password;
	  this.registeredDate = registeredDate;
	  this.isEnabled = isEnabled;
  	}
  	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void setComments(Set<PostComment> comments) {
		this.comments = comments;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<PostComment> getComments() {
		return comments;
	}
	
	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public void setComment(Set<PostComment> comments) {
		this.comments = comments;
	}

	public Set<GroupJoinDetails> getGpDetailsList() {
		return gpDetailsList;
	}

	public void setGpDetailsList(Set<GroupJoinDetails> gpDetailsList) {
		this.gpDetailsList = gpDetailsList;
	}

	
	public Set<ConversationReply> getConversationReply() {
		return conversationReply;
	}

	public void setConversationReply(Set<ConversationReply> conversationReply) {
		this.conversationReply = conversationReply;
	}

  	public Long getId() {
  		return id;
  	}

  	public void setId(Long id) {
  		this.id = id;
  	}

  	public String getUsername() {
  		return username;
  	}

  	public void setUsername(String username) {
	  	this.username = username;
  	}

  	public String getEmail() {
  		return email;
  	}

  	public void setEmail(String email) {
  		this.email = email;
  	}

  	public String getPassword() {
  		return password;
  	}

  	public void setPassword(String password) {
  		this.password = password;
  	}

  	public Set<Role> getRoles() {
  		return roles;
  	}

  	public void setRoles(Set<Role> roles) {
  		this.roles = roles;
  	}

	public Date getRegisteredDate() {
		return registeredDate;
	}
	
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
}