package com.ntth.socialnetwork.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "friendship")
public class FriendShip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "friendship_id", nullable = false)
	private Long friendshipID;
	
	@Column(name = "user_id_1", nullable = false)
	private long userID1;
	
	@Column(name = "user_id_2", nullable = false)
	private long userID2;
	
	@Column(name = "status_id", nullable = false)
	private int statusID;
	
	@Column(name = "date_addfriend", nullable = true)
	private Date dateAddFriend;

	public Long getFriendshipID() {
		return friendshipID;
	}

	public long getUserID1() {
		return userID1;
	}

	public long getUserID2() {
		return userID2;
	}

	public int getStatusID() {
		return statusID;
	}

	public Date getDateAddFriend() {
		return dateAddFriend;
	}

	public void setFriendshipID(Long friendshipID) {
		this.friendshipID = friendshipID;
	}

	public void setUserID1(long userID1) {
		this.userID1 = userID1;
	}

	public void setUserID2(long userID2) {
		this.userID2 = userID2;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}

	public void setDateAddFriend(Date dateAddFriend) {
		this.dateAddFriend = dateAddFriend;
	}

	
	
	
}
