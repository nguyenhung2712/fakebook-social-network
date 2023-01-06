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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "conversation_reply")
public class ConversationReply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cr_id", nullable = false)
	private Long id;

	@NotBlank
	@Column(name = "reply", nullable = false)
	private String reply;
	
	@NotNull
	@Column(name = "time", nullable = false)
	private Timestamp conversationReplyTime;
	
	@NotNull
	@Column(name = "status", nullable = false)
	private int status;
	
	@NotNull
	@Column(name = "detele_status", nullable = false)
	private int deleleStatus;
	
	@ManyToOne
    @JoinColumn(name="user_id_fk", nullable=false)
    private User user;
	
	@ManyToOne
    @JoinColumn(name="c_id_fk", nullable=false)
    private Conversation conversation;


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


	public Timestamp getConversationReplyTime() {
		return conversationReplyTime;
	}


	public int getDeleleStatus() {
		return deleleStatus;
	}


	public void setDeleleStatus(int deleleStatus) {
		this.deleleStatus = deleleStatus;
	}


	public void setConversationReplyTime(Timestamp conversationReplyTime) {
		this.conversationReplyTime = conversationReplyTime;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Conversation getConversation() {
		return conversation;
	}


	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}





	public ConversationReply(Long id, @NotBlank String reply, @NotNull Timestamp conversationReplyTime,
			@NotNull int status, @NotNull int deleleStatus, User user, Conversation conversation) {
		super();
		this.id = id;
		this.reply = reply;
		this.conversationReplyTime = conversationReplyTime;
		this.status = status;
		this.deleleStatus = deleleStatus;
		this.user = user;
		this.conversation = conversation;
	}


	public ConversationReply() {
		super();
	}
	
	
	
}
