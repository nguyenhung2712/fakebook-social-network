package com.ntth.socialnetwork.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "conversation")
public class Conversation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "c_id", nullable = false)
	private Long id;
	
	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Column(name = "status", nullable = false)
	private int status;
	
	@JsonIgnore
	@OneToMany(mappedBy="conversation", cascade = CascadeType.ALL)
  	private Set<ConversationReply> conversationReply;
	
	@JsonIgnore
  	@OneToMany(mappedBy="conver", cascade = CascadeType.ALL)
  	private Set<ConverJoinDetails> cjDetailsList;
	
	

	public Conversation(@NotBlank String name, @NotNull int status) {
		super();
		this.name = name;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<ConversationReply> getConversationReply() {
		return conversationReply;
	}

	public void setConversationReply(Set<ConversationReply> conversationReply) {
		this.conversationReply = conversationReply;
	}

	public Set<ConverJoinDetails> getCjDetailsList() {
		return cjDetailsList;
	}

	public void setCjDetailsList(Set<ConverJoinDetails> cjDetailsList) {
		this.cjDetailsList = cjDetailsList;
	}

	public Conversation() {
		super();
	}
	
	
}
