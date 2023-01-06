package com.ntth.socialnetwork.payload.request;

import java.util.List;

public class AddUsersToConvRequest {
	
	private List<Long> membersID;


	public List<Long> getMembersID() {
		return membersID;
	}

	public void setMembersID(List<Long> membersID) {
		this.membersID = membersID;
	}
}
