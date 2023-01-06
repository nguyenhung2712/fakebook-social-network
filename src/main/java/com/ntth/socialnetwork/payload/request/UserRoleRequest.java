package com.ntth.socialnetwork.payload.request;


public class UserRoleRequest {
	private Long userID;
	private Long roleID;
	public Long getUserID() {
		return userID;
	}
	public Long getRoleID() {
		return roleID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	
}