package com.ntth.socialnetwork.payload.request;

public class LikePostRequest {
	private Long postId;
	private Long userId;
	public Long getPostId() {
		return postId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}