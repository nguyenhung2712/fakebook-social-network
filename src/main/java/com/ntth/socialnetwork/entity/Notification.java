package com.ntth.socialnetwork.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    private User sender;
	
	@ManyToOne
    @JoinColumn(name="recipient_id", nullable=false)
    private User recipient;
	
	public void setSender(User sender) {
		this.sender = sender;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	@Column(name = "activity_type", nullable = false)
	private int activityType;
	
	@Column(name = "time_sent", nullable = false)
	private Date timeSent;
	
	@Column(name = "is_read", nullable = false)
	private int isRead;
	
	@Column(name = "is_check", nullable = false)
	private int isCheck;
	




	@Column(name = "url", nullable = true)
	private String url;
	
	@ManyToOne
	@JoinColumn(name = "sender_profile_id", referencedColumnName = "userprofile_id")
	private UserProfile senderProfile;
	
	public UserProfile getSenderProfile() {
		return senderProfile;
	}

	public void setSenderProfile(UserProfile senderProfile) {
		this.senderProfile = senderProfile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public User getSender() {
		return sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public int getActivityType() {
		return activityType;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public void setIsRead(int i) {
		this.isRead = i;
	}
	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
}