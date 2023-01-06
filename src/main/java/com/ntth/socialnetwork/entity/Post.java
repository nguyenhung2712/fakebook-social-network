package com.ntth.socialnetwork.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false)
	private Long id;

	@NotBlank
	@Size(max = 150)
	@Column(name = "content", nullable = false)
	private String content;

	@NotBlank
	@Column(name = "image", nullable = false)
	private String image;	
  
	@Column(name = "published_date", nullable = false)
	private Timestamp publishedDate;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy="post", cascade = CascadeType.ALL)
  	private Set<PostComment> comment;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "changed_post", 
	  joinColumns = @JoinColumn(name = "post_id"), 
	  inverseJoinColumns = @JoinColumn(name = "history_id"))
	private Set<EditHistory> postsChange;
	
	@Column(name = "group_id")
	private Long group_id;

	public Post() {
		super();
	}

	public Set<PostComment> getComment() {
		return comment;
	}

	public void setComment(Set<PostComment> comment) {
		this.comment = comment;
	}

	public Set<EditHistory> getPostsChange() {
		return postsChange;
	}

	public void setPostsChange(Set<EditHistory> postsChange) {
		this.postsChange = postsChange;
	}

	public Post(@NotBlank @Size(max = 150) String content, @NotBlank String image, Timestamp publishedDate, User user,
			Set<EditHistory> postsChange, Long group_id) {
		super();
		this.content = content;
		this.image = image;
		this.publishedDate = publishedDate;
		this.user = user;
		this.group_id = group_id;
	}

	public Post(@NotBlank @Size(max = 150) String content, @NotBlank String image, Timestamp publishedDate, User user,
			Set<EditHistory> postsChange) {
		super();
		this.content = content;
		this.image = image;
		this.publishedDate = publishedDate;
		this.user = user;
		this.postsChange = postsChange;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Timestamp getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Timestamp published_date) {
		this.publishedDate = published_date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}
	
	
}
