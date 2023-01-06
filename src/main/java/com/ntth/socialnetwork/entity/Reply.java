package com.ntth.socialnetwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "comment_reply")
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_reply_id ", nullable = false)
	private Long id;
	@Column(name = "reply", nullable = false)
	private String reply;
	@Column(name = "date_reply", nullable = false)
	private Timestamp dateReply;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	
   
	@ManyToOne
	@JoinColumn(name = "post_comment_id ", nullable = false)
	private PostComment comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Timestamp getDateReply() {
		return dateReply;
	}

	public void setDateReply(Timestamp dateReply) {
		this.dateReply = dateReply;
	}

	


	public Reply(Long id, String reply,  User user, PostComment comment) {
		super();
		this.id = id;
		this.reply = reply;
		this.user = user;
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PostComment getComment() {
		return comment;
	}

	public void setComment(PostComment comment) {
		this.comment = comment;
	}

	public Reply() {
		super();
	}


	
	
}