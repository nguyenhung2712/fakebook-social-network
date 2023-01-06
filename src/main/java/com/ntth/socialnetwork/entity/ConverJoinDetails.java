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
@Table(name = "joined_conver")
@IdClass(CJPrimaryKey.class)
public class ConverJoinDetails {
	@Id
	@ManyToOne
	@JoinColumn(name = "conv_id", insertable = true, updatable = true)
	private Conversation conver;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = true, updatable = true)
	private User user;
	
	@Column(name = "joined_date", nullable = false)
  	private Date joinedDate;

	public ConverJoinDetails() {
		super();
	}

	public ConverJoinDetails(Conversation conver, User user, Date joinedDate) {
		super();
		this.conver = conver;
		this.user = user;
		this.joinedDate = joinedDate;
	}

	public Conversation getConver() {
		return conver;
	}

	public void setConver(Conversation conver) {
		this.conver = conver;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
	
}

class CJPrimaryKey implements Serializable {
	
	private Long conver;
    private Long user;
    
}