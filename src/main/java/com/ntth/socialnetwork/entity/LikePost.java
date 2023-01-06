package com.ntth.socialnetwork.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "likepost")
@IdClass(LikePrimaryKey.class)
public class LikePost {
	@Id
	@ManyToOne
	@JoinColumn(name = "post_id", insertable = true, updatable = true)
	private Post post;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = true, updatable = true)
	private User user;
	
	@Column(name = "like_date", nullable = false)
  	private Date likeDate;

	public LikePost(Post post, User user, Date likeDate) {
		super();
		this.post = post;
		this.user = user;
		this.likeDate = likeDate;
	}

	public LikePost() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Post getPost() {
		return post;
	}

	public User getUser() {
		return user;
	}

	public Date getLikeDate() {
		return likeDate;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setLikeDate(Date likeDate) {
		this.likeDate = likeDate;
	}
	
	
}

class LikePrimaryKey implements Serializable {
	
	private Long post;
    private Long user;
    
}