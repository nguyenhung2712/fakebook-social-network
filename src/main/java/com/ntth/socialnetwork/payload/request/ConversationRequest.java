package com.ntth.socialnetwork.payload.request;

public class ConversationRequest {
	private String name;
	private int status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setType(int status) {
		this.status = status;
	}
}
