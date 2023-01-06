package com.ntth.socialnetwork.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "edit_history")
public class EditHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", nullable = false)
	private Long id;
	
	@Column(name = "edited_date", nullable = false)
  	private Timestamp editedDate;
	
	@Column(name = "content")
  	private String content;
	
	@Column(name = "image")
  	private String image;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "postsChange")
	private Set<Post> posts;

	public EditHistory() {
		super();
	}

	public EditHistory(Timestamp editedDate, String content, String image, Set<Post> posts) {
		super();
		this.editedDate = editedDate;
		this.content = content;
		this.image = image;
		this.posts = posts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Timestamp editedDate) {
		this.editedDate = editedDate;
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

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	
	
}